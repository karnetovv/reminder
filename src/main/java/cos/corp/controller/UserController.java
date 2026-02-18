package cos.corp.controller;

import cos.corp.domain.entity.User;
import cos.corp.dto.UserUpdateDto;
import cos.corp.dto.UserRespDto;
import cos.corp.mapper.UserMapper;
import cos.corp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("api/v1/users/")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("me")
    public ResponseEntity<UserRespDto> getProfile(Authentication authentication) {
        User user = userService.getInternalUserId(authentication);
        return ResponseEntity.ok(userMapper.toUserResponse(user));
    }

    @PutMapping("me")
    public ResponseEntity<UserRespDto> updateProfile(@RequestBody UserUpdateDto userUpdateDto, Authentication authentication) {
        User current = userService.getInternalUserId(authentication);
        User updated = userService.updateProfile(current.getId(), userUpdateDto);
        return ResponseEntity.ok(userMapper.toUserResponse(updated));
    }

    @DeleteMapping("me")
    public ResponseEntity<UserRespDto> deleteProfile(Authentication authentication) {
        String externalId = ((JwtAuthenticationToken) authentication).getToken().getSubject();
        userService.deleteInternalUser(externalId);
        return ResponseEntity.noContent().build();
    }
}
