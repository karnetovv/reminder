package cos.corp.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReminderUpdateReqDto (
        @NotNull
        Long id,
        String title,
        String description,
        LocalDateTime remind
) {
}
