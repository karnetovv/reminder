package cos.corp.service.sender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class TelegramNotificationSender {

    private final RestTemplate restTemplate;

    @Value("${app.notification.telegram.bot-token}")
    private String botToken;

    @Value("${app.notification.telegram.api-url}")
    private String apiUrl;

    public TelegramNotificationSender(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void send(String chatId, String text) {
        String url = apiUrl + "/bot" + botToken + "/sendMessage";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("chat_id", chatId);
        requestBody.put("text", text);

        restTemplate.postForEntity(url, requestBody, String.class);
    }
}
