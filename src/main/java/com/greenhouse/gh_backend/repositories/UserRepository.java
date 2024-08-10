package com.greenhouse.gh_backend.repositories;

import com.greenhouse.gh_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    User findByResetPasswordToken(String resetPasswordToken);
    User findByVerificationToken(String verificationToken);

}
