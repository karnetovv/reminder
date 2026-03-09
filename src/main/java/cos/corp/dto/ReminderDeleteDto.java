package cos.corp.dto;

import jakarta.validation.constraints.NotNull;

public record ReminderDeleteDto(
        @NotNull
        Long reminderId
) {
}
