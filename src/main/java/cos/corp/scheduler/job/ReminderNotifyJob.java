package cos.corp.scheduler.job;

import cos.corp.domain.entity.Reminder;
import cos.corp.domain.repository.ReminderRepository;
import cos.corp.exception.NotFoundException;
import cos.corp.service.NotificationService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ReminderNotifyJob implements Job {

    private final ReminderRepository reminderRepository;
    private final NotificationService notificationService;

    public ReminderNotifyJob(ReminderRepository reminderRepository,
                             NotificationService notificationService) {
        this.reminderRepository = reminderRepository;
        this.notificationService = notificationService;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Long reminderId = context.getMergedJobDataMap().getLong("reminderId");

        Reminder reminder = reminderRepository.findByIdWithUser(reminderId)
                .orElseThrow(() -> new NotFoundException("Напоминание не найдено."));

        notificationService.sendNotification(reminder);
    }
}
