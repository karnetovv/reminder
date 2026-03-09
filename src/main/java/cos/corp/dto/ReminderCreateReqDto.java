package cos.corp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReminderCreateReqDto(
        @NotBlank
        String title,
        String description,
        @NotNull
        LocalDateTime remind
) {
}
