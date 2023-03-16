package ru.otus.profileservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserSignUpResultDto {
    private Boolean isSuccess;
    private Long id;
}
