package ru.otus.profileservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {
    private String login;
    private String oldLogin;
    private String password;
}
