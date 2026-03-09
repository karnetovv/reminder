package cos.corp.service;

import cos.corp.domain.entity.Reminder;
import cos.corp.dto.ReminderListRespDto;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.time.LocalTime;

public interface ReminderService {

    public Reminder createReminder(Reminder reminder, Long userId);

    public Reminder updateReminder(Reminder reminder, Long userId);

    public Reminder deleteReminder(Long reminderId, Long userId);

    ReminderListRespDto listReminders(Long userId,
                                      Pageable pageable,
                                      String title,
                                      String description,
                                      LocalDate date,
                                      LocalTime time);

}
