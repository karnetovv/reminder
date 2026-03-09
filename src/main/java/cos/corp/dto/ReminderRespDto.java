package cos.corp.dto;

import java.time.LocalDateTime;

public record ReminderRespDto(
    Long id,
    String title,
    String description,
    LocalDateTime remind
) {
}
