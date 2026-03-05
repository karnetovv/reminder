package cos.corp.service;

import cos.corp.domain.entity.Reminder;

public interface ReminderService {

    public Reminder createReminder(Reminder reminder, Long userId);

    public Reminder updateReminder(Reminder reminder, Long userId);

    public Reminder deleteReminder(Long reminderId, Long userId);



}
