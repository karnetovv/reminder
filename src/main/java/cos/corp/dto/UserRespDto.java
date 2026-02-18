package cos.corp.dto;

import java.util.Date;

public record UserRespDto (
        Long id,
        String username,
        String email,
        String telegramId
) {
}
