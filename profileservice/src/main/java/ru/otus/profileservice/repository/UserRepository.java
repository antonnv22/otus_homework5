package ru.otus.profileservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.profileservice.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);

    Boolean existsByLogin(String login);
}
