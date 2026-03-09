package cos.corp.telegram;

import cos.corp.telegram.dto.TelegramGetUpdatesResponse;
import cos.corp.telegram.dto.TelegramMessageDto;
import cos.corp.telegram.dto.TelegramUpdateDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TelegramPollingService {

    private final RestTemplate restTemplate;
    private final TelegramUserLinkService telegramUserLinkService;

    @Value("${app.notification.telegram.bot-token}")
    private String botToken;

    @Value("${app.notification.telegram.api-url}")
    private String apiUrl;

    @Value("${app.notification.telegram.enabled:true}")
    private boolean telegramEnabled;

    private volatile long offset = 0L;

    public TelegramPollingService(RestTemplate restTemplate,
                                  TelegramUserLinkService telegramUserLinkService) {
        this.restTemplate = restTemplate;
        this.telegramUserLinkService = telegramUserLinkService;
    }

    @PostConstruct
    public void init() {
        this.offset = 0L;
    }

    @Scheduled(fixedDelayString = "${app.notification.telegram.poll-delay-ms:5000}")
    public void pollUpdates() {
        if (!telegramEnabled || botToken == null || botToken.isBlank()) {
            return;
        }

        String url = apiUrl + "/bot" + botToken + "/getUpdates?timeout=10&offset=" + offset;

        TelegramGetUpdatesResponse response = restTemplate.getForObject(url, TelegramGetUpdatesResponse.class);

        if (response == null || !response.isOk() || response.getResult() == null) {
            return;
        }

        for (TelegramUpdateDto update : response.getResult()) {
            processUpdate(update);
            offset = update.getUpdate_id() + 1;
        }
    }

    private void processUpdate(TelegramUpdateDto update) {
        if (update.getMessage() == null) {
            return;
        }

        TelegramMessageDto message = update.getMessage();

        if (message.getText() == null || message.getChat() == null || message.getChat().getId() == null) {
            return;
        }

        String text = message.getText().trim();

        if (!text.startsWith("/start")) {
            return;
        }

        // ожидаем формат: /start <externalId>
        String[] parts = text.split("\\s+", 2);

        if (parts.length < 2 || parts[1].isBlank()) {
            sendHelpMessage(message.getChat().getId());
            return;
        }

        String externalId = parts[1].trim();
        Long chatId = message.getChat().getId();

        telegramUserLinkService.linkTelegramChat(externalId, chatId);
        sendSuccessMessage(chatId);
    }

    private void sendHelpMessage(Long chatId) {
        String url = apiUrl + "/bot" + botToken + "/sendMessage";

        String text = """
                Для подключения нотификаций через Telegram, отправьте команду в формате:
                /start код нотификации
                """;

        restTemplate.postForObject(
                url,
                new SendMessageRequest(String.valueOf(chatId), text),
                String.class
        );
    }

    private void sendSuccessMessage(Long chatId) {
        String url = apiUrl + "/bot" + botToken + "/sendMessage";

        String text = "Уведомления через Telegram подключены успешно.";

        restTemplate.postForObject(
                url,
                new SendMessageRequest(String.valueOf(chatId), text),
                String.class
        );
    }

    private record SendMessageRequest(String chat_id, String text) {
    }
}