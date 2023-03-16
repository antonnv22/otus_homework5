package ru.otus.profileservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.profileservice.dto.UserDto;
import ru.otus.profileservice.dto.UserLoginDto;
import ru.otus.profileservice.dto.UserLoginResponse;
import ru.otus.profileservice.dto.UserSignUpResultDto;
import ru.otus.profileservice.service.UserService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping(value = "signUp")
    public ResponseEntity<UserSignUpResultDto> insert(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.insert(userDto));
    }

    @PostMapping(value = "login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginDto userLoginDto) {
        return ResponseEntity.ok(userService.login(userLoginDto));
    }

    @PutMapping(value = "update/{login}")
    public ResponseEntity<Void> update(@RequestBody UserDto userDto, @PathVariable String login) {
        userService.update(userDto, login);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "{oldLogin}")
    public ResponseEntity<UserDto> getByLogin(@PathVariable String oldLogin) {
        return ResponseEntity.ok(userService.findByLogin(oldLogin));
    }
}
