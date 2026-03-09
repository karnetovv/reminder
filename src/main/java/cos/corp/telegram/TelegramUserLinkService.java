package cos.corp.telegram;

import cos.corp.domain.entity.User;
import cos.corp.domain.repository.UserRepository;
import cos.corp.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TelegramUserLinkService {

    private final UserRepository userRepository;

    public TelegramUserLinkService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void linkTelegramChat(String externalId, Long chatId) {
        User user = userRepository.findByExternalId(externalId)
                .orElseThrow(() -> new NotFoundException("User not found for externalId: " + externalId));

        user.setTelegramId(String.valueOf(chatId));
        userRepository.save(user);
    }
}
