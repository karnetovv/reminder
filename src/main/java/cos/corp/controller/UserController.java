package cos.corp.controller;

import cos.corp.dto.UserCreateDto;
import cos.corp.dto.UserRespDto;
import cos.corp.mapper.UserMapper;
import cos.corp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

//    @PostMapping
//    public ResponseEntity<UserRespDto> addUser(@RequestBody UserCreateDto user) {
//
//    }
}
