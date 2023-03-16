package ru.otus.profileservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import ru.otus.profileservice.model.UserLogin;

public interface UserLoginRepository extends JpaRepository<UserLogin, String> {
    Boolean existsByLogin(String login);
    @Modifying
    void deleteByLogin(String login);
}
