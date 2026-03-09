package cos.corp.service.sender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationSender {

    private final JavaMailSender mailSender;

    @Value("${app.notification.email.from:no-reply@reminder.local}")
    private String from;

    @Value("${app.notification.email.subject-prefix:[Reminder]}")
    private String subjectPrefix;

    public EmailNotificationSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subjectPrefix + " " + subject);
        message.setText(text);
        mailSender.send(message);
    }
}
