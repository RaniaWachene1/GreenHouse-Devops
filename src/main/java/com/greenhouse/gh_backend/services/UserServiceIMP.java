package com.greenhouse.gh_backend.services;

import com.greenhouse.gh_backend.entities.User;
import com.greenhouse.gh_backend.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceIMP implements IUser {
    @Autowired
    private UserRepository userRepository;


    private final List<String> disallowedDomains = Arrays.asList(
            "gmail.com", "yahoo.com", "hotmail.com", "aol.com", "outlook.com",
            "icloud.com", "mail.com", "gmx.com", "yandex.com", "protonmail.com"
    );
    @Override
    public User addUser(User user) {
        if (!isCompanyEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email must be a company email");
        }
        try {

            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add user. Error: " + e.getMessage());
        }
    }


    @Override
    public User retrieveById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void updateUser(Long userId, User updateUser) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));


        if (updateUser.getEmail() != null) {
            existingUser.setEmail(updateUser.getEmail());
        }
        if (updateUser.getFirstName() != null) {
            existingUser.setFirstName(updateUser.getFirstName());
        }
        if (updateUser.getLastName() != null) {
            existingUser.setLastName(updateUser.getLastName());
        }
        if (updateUser.getCompanyName() != null) {
            existingUser.setCompanyName(updateUser.getCompanyName());
        }
        if (updateUser.getSector() != null) {
            existingUser.setSector(updateUser.getSector());
        }
        if (updateUser.getIndustry() != null) {
            existingUser.setIndustry(updateUser.getIndustry());
        }
        if (updateUser.getRevenue() != null) {
            existingUser.setRevenue(updateUser.getRevenue());
        }
        if (updateUser.getHeadquarters() != null) {
            existingUser.setHeadquarters(updateUser.getHeadquarters());
        }
        if (updateUser.getCurrency() != null) {
            existingUser.setCurrency(updateUser.getCurrency());
        }
        if (updateUser.getBaseYear() != 0) {
            existingUser.setBaseYear(updateUser.getBaseYear());
        }
        if (updateUser.getEndYear() != 0) {
            existingUser.setEndYear(updateUser.getEndYear());
        }
        if (updateUser.getReductionAmbition() != 0) {
            existingUser.setReductionAmbition(updateUser.getReductionAmbition());
        }
        if (updateUser.getTargetReduction() != 0) {
            existingUser.setTargetReduction(updateUser.getTargetReduction());
        }
        existingUser.setProfileComplete(updateUser.isProfileComplete());
        userRepository.save(existingUser);
    }



    @Override
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }



    public void generatePasswordResetToken(User user) {
        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setResetPasswordTokenExpiry(new Date(System.currentTimeMillis() + 3600000)); // Token expiry in 1 hour
        userRepository.save(user);
    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token);
        if (user != null && user.getResetPasswordTokenExpiry().after(new Date())) {
            user.setPassword(newPassword);
            user.setResetPasswordToken(null);
            user.setResetPasswordTokenExpiry(null);
            userRepository.save(user);

        } else {
            // Invalid or expired token, notify user
        }
    }




    @Override
    public boolean verifyEmail(String verificationToken) {
        User user = userRepository.findByVerificationToken(verificationToken);

        if (user != null) {
            user.setVerified(true);
            user.setVerificationToken(null);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    public boolean isProfileComplete(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        return user.isProfileComplete();
    }

    public User completeProfile(Long userId, User updateUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        user.setSector(updateUser.getSector());
        user.setIndustry(updateUser.getIndustry());
        user.setRevenue(updateUser.getRevenue());
        user.setHeadquarters(updateUser.getHeadquarters());
        user.setCurrency(updateUser.getCurrency());
        user.setProfileComplete(true);
        return userRepository.save(user); // Return the updated user
    }
    public boolean isCompanyEmail(String email) {
        String domain = email.substring(email.indexOf("@") + 1).toLowerCase();
        return domain.contains(".") && !disallowedDomains.contains(domain);
    }
}