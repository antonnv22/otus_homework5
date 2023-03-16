package ru.otus.profileservice.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.profileservice.dto.UserLoginDto;
import ru.otus.profileservice.model.UserLogin;
import ru.otus.profileservice.security.PasswordUtil;

@Component
@RequiredArgsConstructor
public class UserLoginMapper {
    private final PasswordUtil securityUtil;

    public UserLogin toEntity(UserLoginDto userLoginDto) {
        return UserLogin.builder()
                .login(userLoginDto.getLogin())
                .password(securityUtil.getSecuredPassword(userLoginDto.getPassword()))
                .build();
    }
}
