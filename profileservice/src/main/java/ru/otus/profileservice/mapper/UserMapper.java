package ru.otus.profileservice.mapper;

import org.mapstruct.Mapper;
import ru.otus.profileservice.dto.UserLoginDto;
import ru.otus.profileservice.model.User;
import ru.otus.profileservice.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(UserDto userDto);

    UserLoginDto toUserLoginDto(UserDto userDto);
}
