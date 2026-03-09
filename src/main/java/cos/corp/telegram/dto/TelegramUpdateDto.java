package cos.corp.telegram.dto;

public class TelegramUpdateDto {

    private Long update_id;
    private TelegramMessageDto message;

    public Long getUpdate_id() {
        return update_id;
    }

    public void setUpdate_id(Long update_id) {
        this.update_id = update_id;
    }

    public TelegramMessageDto getMessage() {
        return message;
    }

    public void setMessage(TelegramMessageDto message) {
        this.message = message;
    }
}
