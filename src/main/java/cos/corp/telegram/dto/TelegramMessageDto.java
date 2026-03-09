package cos.corp.telegram.dto;

public class TelegramMessageDto {

    private Long message_id;
    private TelegramFromDto from;
    private TelegramChatDto chat;
    private String text;

    public Long getMessage_id() {
        return message_id;
    }

    public void setMessage_id(Long message_id) {
        this.message_id = message_id;
    }

    public TelegramFromDto getFrom() {
        return from;
    }

    public void setFrom(TelegramFromDto from) {
        this.from = from;
    }

    public TelegramChatDto getChat() {
        return chat;
    }

    public void setChat(TelegramChatDto chat) {
        this.chat = chat;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}