package cos.corp.dto;

import java.util.Date;

public class UserRespDto {

    public Long id;
    public String username;
    public String email;
    public String telegramId;
    public Date createdAt;


    public UserRespDto(Long id, String username, String email, String telegramId, Date createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.telegramId = telegramId;
        this.createdAt = createdAt;
    }

    public UserRespDto() {
    }
}
