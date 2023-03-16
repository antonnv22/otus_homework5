package ru.otus.profileservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.profileservice.dto.UserLoginDto;
import ru.otus.profileservice.dto.UserLoginResponse;
import ru.otus.profileservice.exceptions.NotFoundUserException;
import ru.otus.profileservice.mapper.UserLoginMapper;
import ru.otus.profileservice.model.UserLogin;
import ru.otus.profileservice.repository.UserLoginRepository;
import ru.otus.profileservice.security.JwtTokenService;
import ru.otus.profileservice.security.PasswordUtil;

import java.util.Objects;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class UserLoginService {
    private final UserLoginRepository userLoginRepository;
    private final UserLoginMapper userLoginMapper;
    private final PasswordUtil passwordUtil;
    private final JwtTokenService jwtTokenService;

    @Transactional
    public void createCredentials(UserLoginDto userLoginDto) {
        userLoginRepository.save(userLoginMapper.toEntity(userLoginDto));
    }

    @Transactional
    public void editCredentials(UserLoginDto userLoginDto, String oldLogin) {
        checkUserLoginNotFound(oldLogin);
        userLoginRepository.deleteByLogin(oldLogin);
        userLoginRepository.save(userLoginMapper.toEntity(userLoginDto));
    }

    public UserLoginResponse login(UserLoginDto userLoginDto) {
        UserLogin userLogin = userLoginRepository.findById(userLoginDto.getLogin())
                .orElseThrow(() -> new NotFoundUserException("User not found"));
        String token = null;
        if (Objects.equals(userLogin.getPassword(), passwordUtil.getSecuredPassword(userLoginDto.getPassword()))) {
            token = jwtTokenService.generate(userLoginDto.getLogin());
        }
        return UserLoginResponse.builder()
                .isSuccess(Objects.nonNull(token))
                .token(token)
                .build();
    }

    private void checkUserLoginNotFound(String login) {
        if (!userLoginRepository.existsByLogin(login)) {
            throw new NotFoundUserException(String.format("User with login=%s not found in database", login));
        }
    }
}
