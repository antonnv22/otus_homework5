package ru.otus.profileservice.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.profileservice.dto.UserDto;
import ru.otus.profileservice.dto.UserLoginDto;
import ru.otus.profileservice.dto.UserLoginResponse;
import ru.otus.profileservice.dto.UserSignUpResultDto;
import ru.otus.profileservice.exceptions.DuplicateUserException;
import ru.otus.profileservice.exceptions.NotFoundUserException;
import ru.otus.profileservice.mapper.UserMapper;
import ru.otus.profileservice.model.User;
import ru.otus.profileservice.repository.UserRepository;

import java.util.Objects;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserLoginService userLoginService;

    public UserLoginResponse login(UserLoginDto userLoginDto) {
        return userLoginService.login(userLoginDto);
    }

    public UserDto findByLogin(String login) {
        return userRepository.findByLogin(login)
                .map(userMapper::toDto)
                .orElseThrow(() -> new NotFoundUserException(String.format("Not found user for login=%s", login)));
    }

    @Transactional(rollbackFor = Exception.class)
    public UserSignUpResultDto insert(UserDto userDto) {
        checkUserAlreadyExists(userDto);
        checkNewUserId(userDto);
        User savedUser = userRepository.save(userMapper.toEntity(userDto));
        userLoginService.createCredentials(userMapper.toUserLoginDto(userDto));
        return UserSignUpResultDto.builder()
                .id(savedUser.getId())
                .isSuccess(true)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(UserDto userDto, String oldLogin) {
        checkUserNotFound(oldLogin);
        userRepository.save(userMapper.toEntity(userDto));
        if (Objects.nonNull(userDto.getPassword()) || !oldLogin.equals(userDto.getLogin())) {
            userLoginService.editCredentials(userMapper.toUserLoginDto(userDto), oldLogin);
        }
    }

    private void checkUserNotFound(String oldLogin) {
        if (!userRepository.existsByLogin(oldLogin)) {
            throw new NotFoundUserException(String.format("User with login=%s not found in database for update", oldLogin));
        }
    }

    private void checkNewUserId(UserDto userDto) {
        if (Objects.nonNull(userDto.getId())) {
            throw new IllegalArgumentException("You shouldn't pass id for new user");
        }
    }

    private void checkUserAlreadyExists(UserDto userDto) {
        if (userRepository.existsByLogin(userDto.getLogin())) {
            throw new DuplicateUserException("User with login=%s already exists ");
        }
    }
}
