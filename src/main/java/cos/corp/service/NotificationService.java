package cos.corp.service;

import cos.corp.domain.entity.Reminder;

public interface NotificationService {

    void sendNotification(Reminder reminder);
}
