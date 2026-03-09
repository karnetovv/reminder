package cos.corp.controller;

import cos.corp.domain.entity.User;
import cos.corp.dto.UserUpdateDto;
import cos.corp.dto.UserRespDto;
import cos.corp.mapper.UserMapper;
import cos.corp.service.UserService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("me")
    public ResponseEntity<UserRespDto> getProfile(Authentication authentication) {
        User user = userService.getInternalUser(authentication);
        return ResponseEntity.ok(userMapper.toUserResponse(user));
    }

    @PutMapping("me")
    public ResponseEntity<UserRespDto> updateProfile(@Valid @RequestBody UserUpdateDto userUpdateDto, Authentication authentication) {
        User current = userService.getInternalUser(authentication);
        User updated = userService.updateProfile(current.getId(), userUpdateDto);
        return ResponseEntity.ok(userMapper.toUserResponse(updated));
    }

    @DeleteMapping("me")
    public ResponseEntity<Void> deleteProfile(Authentication authentication) {
        String externalId = ((JwtAuthenticationToken) authentication).getToken().getSubject();
        userService.deleteInternalUser(externalId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/external-id")
    public ResponseEntity<String> getExternalId(Authentication authentication) {
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
        return ResponseEntity.ok(jwtAuth.getToken().getSubject());
    }
}
