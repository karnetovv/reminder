package cos.corp.service.impl;

import cos.corp.domain.entity.Reminder;
import cos.corp.domain.entity.User;
import cos.corp.service.NotificationService;
import cos.corp.service.sender.EmailNotificationSender;
import cos.corp.service.sender.TelegramNotificationSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final EmailNotificationSender emailNotificationSender;
    private final TelegramNotificationSender telegramNotificationSender;

    public NotificationServiceImpl(EmailNotificationSender emailNotificationSender, TelegramNotificationSender telegramNotificationSender) {
        this.emailNotificationSender = emailNotificationSender;
        this.telegramNotificationSender = telegramNotificationSender;
    }

    @Override
    public void sendNotification(Reminder reminder) {
        User user = reminder.getUser();

        String subject = reminder.getTitle();
        String text = reminder.getDescription() != null
                ? reminder.getDescription()
                : "Новое напоминание.";

        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            emailNotificationSender.send(user.getEmail(), subject, text);
        }

        if (user.getTelegramId() != null && !user.getTelegramId().isBlank()) {
            telegramNotificationSender.send(user.getTelegramId(), subject + "\n" + text);
        }

    }
}
