package cos.corp.service.impl;

import cos.corp.domain.entity.User;
import cos.corp.domain.repository.UserRepository;
import cos.corp.dto.UserUpdateDto;
import cos.corp.exception.NotFoundException;
import cos.corp.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public User getInternalUser(Authentication authentication) {

        JwtAuthenticationToken jwtAuth = extractJwt(authentication);

        String externalId = jwtAuth.getToken().getSubject(); // sub
        if (externalId == null || externalId.isBlank()) {
            throw new IllegalStateException("JWT токен не содержит subject.");
        }

        String username = Optional.ofNullable(
                        jwtAuth.getToken().getClaimAsString("preferred_username")
                ).filter(s -> !s.isBlank())
                .orElse(externalId);

        String email = jwtAuth.getToken().getClaimAsString("email");

        return userRepository.findByExternalId(externalId)
                .map(existing -> syncProfile(existing, username, email))
                .orElseGet(() -> createInternalUser(externalId, username, email));
    }

    @Override
    @Transactional
    public User createInternalUser(String externalId, String username, String email) {

        User user = new User();
        user.setExternalId(externalId);
        user.setUsername(username);
        user.setEmail(email);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteInternalUser(String externalId) {
        userRepository.deleteByExternalId(externalId);
    }

    @Override
    @Transactional
    public User updateProfile(Long userId, UserUpdateDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        if (dto.email() != null) user.setEmail(dto.email());
        if (dto.telegramId()!= null) user.setTelegramId(dto.telegramId());

        return userRepository.save(user);
    }

    private User syncProfile(User user, String username, String email) {

        boolean changed = false;

        if (username != null && !username.isBlank()
                && !Objects.equals(user.getUsername(), username)) {
            user.setUsername(username);
            changed = true;
        }

        if (email != null && !email.isBlank()
                && !Objects.equals(user.getEmail(), email)) {
            user.setEmail(email);
            changed = true;
        }

        return changed ? userRepository.save(user) : user;
    }

    private JwtAuthenticationToken extractJwt(Authentication authentication) {
        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            throw new IllegalStateException("JWT authentication required");
        }
        return jwtAuth;
    }
}
