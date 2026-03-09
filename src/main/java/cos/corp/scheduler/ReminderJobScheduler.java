package cos.corp.scheduler;

import cos.corp.domain.entity.Reminder;
import cos.corp.scheduler.job.ReminderNotifyJob;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;

@Service
public class ReminderJobScheduler {

    private final Scheduler scheduler;

    public ReminderJobScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void scheduleReminder(Reminder reminder){
        try {
            JobDetail jobDetail = JobBuilder.newJob(ReminderNotifyJob.class)
                    .withIdentity(buildJobKey(reminder.getId()))
                    .usingJobData("reminderId", reminder.getId())
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(buildTriggerKey(reminder.getId()))
                    .startAt(Date.from(reminder.getRemind()
                            .atZone(ZoneId.systemDefault())
                            .toInstant()))
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to schedule reminder job", e);
        }
    }

    public void rescheduleReminder(Reminder reminder) {
        try {
            TriggerKey triggerKey = buildTriggerKey(reminder.getId());

            Trigger newTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .startAt(Date.from(reminder.getRemind()
                            .atZone(ZoneId.systemDefault())
                            .toInstant()))
                    .build();

            scheduler.rescheduleJob(triggerKey, newTrigger);
        } catch (SchedulerException e) {

        }
    }

    public void deleteReminderJob(Reminder reminder){
        try {
            scheduler.deleteJob(buildJobKey(reminder.getId()));
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to delete reminder job", e);
        }
    }

    private JobKey buildJobKey(Long reminderId) {
        return JobKey.jobKey("reminder:" + reminderId);
    }

    private TriggerKey buildTriggerKey(Long reminderId) {
        return TriggerKey.triggerKey("reminder:" + reminderId + ":trigger");
    }
}
