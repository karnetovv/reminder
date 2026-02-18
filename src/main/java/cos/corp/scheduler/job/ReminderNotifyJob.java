package cos.corp.scheduler.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ReminderNotifyJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        /*
        Читаем мапу джобов из нее получаем напоминание и отправляем
         */
        Long reminderId = jobExecutionContext.getMergedJobDataMap().getLong("reminderId");
    }
}
