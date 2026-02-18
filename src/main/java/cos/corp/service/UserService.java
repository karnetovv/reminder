package cos.corp.service;

import cos.corp.domain.entity.User;
import cos.corp.dto.UserUpdateDto;
import org.springframework.security.core.Authentication;

public interface UserService {

    User getInternalUserId(Authentication authentication);

    User createInternalUser(String externalId, String username, String email);

    void deleteInternalUser(String externalId);

    User updateProfile(Long userId, UserUpdateDto dto);
}
