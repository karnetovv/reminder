package cos.corp.dto;

import java.util.Date;

public record ReminderRespDto(
    Long id,
    String title,
    String description,
    Date reminderDate,
    String remindStatus
) {
}
