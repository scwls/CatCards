const { onValueCreated, onValueDeleted } = require("firebase-functions/v2/database");
const { onSchedule } = require("firebase-functions/v2/scheduler");
const { onCall, HttpsError } = require("firebase-functions/v2/https");
const admin = require("firebase-admin");
const crypto = require("crypto");
const { Resend } = require('resend');

admin.initializeApp();

// ВНИМАНИЕ: Ключ Resend удален для безопасности!
// Для работы функций необходимо задать ключ через переменные окружения Firebase.
// Выполните команду в консоли: firebase functions:config:set resend.key="ВАШ_КЛЮЧ"
const resend = new Resend(process.env.RESEND_API_KEY || 'dummy_key');

const CODE_TTL_MS = 10 * 60 * 1000;
const hashCode = (email, code) => crypto.createHash("sha256")
  .update(`${email}:${code}`)
  .digest("hex");

exports.sendEmailVerificationCode = onCall(async (request) => {
  const email = String(request.data?.email || "").trim().toLowerCase();
  if (!/^\S+@\S+\.\S+$/.test(email)) {
    throw new HttpsError("invalid-argument", "Введите корректный email");
  }
  const code = String(Math.floor(100000 + Math.random() * 900000));
  const now = Date.now();
  // В Firebase Realtime Database ключи не могут содержать спецсимволы (точки, доллары и т.д.).
  // Используем base64 для безопасного кодирования email-а, чтобы использовать его как ключ ветки.
  const safeEmailKey = Buffer.from(email).toString('base64').replace(/\//g, '_').replace(/\+/g, '-').replace(/=/g, '');
  const ref = admin.database().ref(`emailVerificationCodes/${safeEmailKey}`);
  const existing = (await ref.get()).val();
  if (existing?.lastSentAt && now - existing.lastSentAt < 60 * 1000) {
    throw new HttpsError("resource-exhausted", "Повторно запросить код можно через минуту");
  }

  await ref.set({ hash: hashCode(email, code), expiresAt: now + CODE_TTL_MS, lastSentAt: now });

  try {
    const data = await resend.emails.send({
      from: 'onboarding@resend.dev', // ОБЯЗАТЕЛЬНО должен быть этот адрес для тестового аккаунта Resend
      to: email,
      subject: 'Код подтверждения CardEng',
      html: `
        <div style="font-family: Arial, sans-serif; padding: 20px;">
          <h2>Добро пожаловать в CardEng!</h2>
          <p>Ваш код подтверждения:</p>
          <h1 style="color: #445285; letter-spacing: 5px;">${code}</h1>
          <p style="color: #666; font-size: 12px;">Код действителен 10 минут.</p>
        </div>
      `
    });
    console.log(`Письмо успешно отправлено на ${email}. ID: ${data.id}`);
    return { sent: true };
  } catch (error) {
    console.error(`Ошибка отправки письма через Resend на ${email}`, error);
    throw new HttpsError("internal", "Не удалось отправить письмо с кодом. " + error.message);
  }
});

exports.verifyEmailVerificationCode = onCall(async (request) => {
  const email = String(request.data?.email || "").trim().toLowerCase();
  const code = String(request.data?.code || "").trim();

  const safeEmailKey = Buffer.from(email).toString('base64').replace(/\//g, '_').replace(/\+/g, '-').replace(/=/g, '');
  const ref = admin.database().ref(`emailVerificationCodes/${safeEmailKey}`);
  const snapshot = await ref.get();
  const record = snapshot.val();
  if (!record || Date.now() > record.expiresAt || hashCode(email, code) !== record.hash) {
    throw new HttpsError("invalid-argument", "Неверный или просроченный код");
  }
  await ref.remove();
  return { verified: true };
});

exports.sendNotification = onValueCreated("/notificationRequests/{nickname}/{requestId}", async (event) => {
  const request = event.data.val();
  const nickname = event.params.nickname;
  const tokenSnapshot = await admin.database().ref(`users/${nickname}/fcmToken`).get();
  const token = tokenSnapshot.val();

  if (!token || typeof request !== "string" || !request.trim()) {
    console.warn(`Notification skipped for ${nickname}: missing token or invalid request`);
    await event.data.ref.remove();
    return;
  }

  try {
    await admin.messaging().send({
      token,
      notification: {
        title: "Сообщение от разработчика",
        body: request
      },
      android: {
        priority: "high",
        notification: {
          channelId: "admin_msg_v2",
          sound: "default",
          defaultSound: true,
          defaultVibrateTimings: true,
          notificationPriority: "PRIORITY_HIGH"
        }
      },
      data: { message: request }
    });
    console.log(`Notification sent to ${nickname}`);
    await event.data.ref.remove();
  } catch (error) {
    console.error(`FCM send failed for ${nickname}`, error);
    if (error.code === "messaging/registration-token-not-registered" ||
        error.code === "messaging/invalid-registration-token") {
      await admin.database().ref(`users/${nickname}/fcmToken`).remove();
      await event.data.ref.remove();
      return;
    }
    throw error;
  }
});

exports.deleteUserStorage = onValueDeleted("/users/{nickname}", async (event) => {
  const nickname = event.params.nickname;
  await admin.storage().bucket().deleteFiles({ prefix: `decks/${nickname}/` });
  await admin.database().ref(`notificationRequests/${nickname}`).remove();
});

exports.deleteExpiredErrorReports = onSchedule("every 1 hours", async () => {
  const bucket = admin.storage().bucket();
  const [files] = await bucket.getFiles({ prefix: "error_reports/" });
  const expirationTime = Date.now() - 2 * 60 * 60 * 1000;

  await Promise.all(files.map(async (file) => {
    const [metadata] = await file.getMetadata();
    const updatedAt = Date.parse(metadata.updated || metadata.timeCreated || "");
    if (Number.isFinite(updatedAt) && updatedAt < expirationTime) {
      await file.delete();
    }
  }));
});
