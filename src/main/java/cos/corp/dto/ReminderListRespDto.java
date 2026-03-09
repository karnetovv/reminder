package cos.corp.dto;

import java.util.List;

public record ReminderListRespDto(
        long total,
        int current,
        int size,
        List<ReminderRespDto> items
) {
}
