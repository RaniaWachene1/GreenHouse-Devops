package com.greenhouse.gh_backend;

import com.greenhouse.gh_backend.entities.User;
import com.greenhouse.gh_backend.repositories.UserRepository;
import com.greenhouse.gh_backend.services.UserServiceIMP;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceIMPTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceIMP userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);  // Initializes mocks and injects them
    }

    // Test for adding a user with a valid company email
    @Test
    void testAddUser_ValidCompanyEmail() {
        // Arrange
        User user = new User();
        user.setEmail("user@company.com");

        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User result = userService.addUser(user);

        // Assert
        assertNotNull(result);
        assertEquals("user@company.com", result.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    // Test for adding a user with a non-company email
    @Test
    void testAddUser_InvalidEmail() {
        // Arrange
        User user = new User();
        user.setEmail("user@gmail.com");

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.addUser(user);
        });
        assertEquals("Email must be a company email", exception.getMessage());
        verify(userRepository, times(0)).save(any(User.class));  // User shouldn't be saved
    }

    // Test for retrieving user by ID
    @Test
    void testRetrieveUserById_Success() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setIdUser(userId);
        user.setEmail("user@company.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        User result = userService.retrieveById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getIdUser());
        verify(userRepository, times(1)).findById(userId);
    }

    // Test for retrieving user by ID when user does not exist
    @Test
    void testRetrieveUserById_UserNotFound() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        User result = userService.retrieveById(userId);

        // Assert
        assertNull(result);  // Should return null when user is not found
    }

    // Test for updating user
    @Test
    void testUpdateUser_Success() {
        // Arrange
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setIdUser(userId);
        existingUser.setEmail("old@company.com");

        User updateUser = new User();
        updateUser.setEmail("new@company.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        // Act
        userService.updateUser(userId, updateUser);

        // Assert
        assertEquals("new@company.com", existingUser.getEmail());  // Verify that the email was updated
        verify(userRepository, times(1)).save(existingUser);
    }

    // Test for deleting a user
    @Test
    void testDeleteUser_Success() {
        // Arrange
        Long userId = 1L;

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);  // Verify that the delete method was called
    }

    // Test for generating password reset token
    @Test
    void testGeneratePasswordResetToken_Success() {
        // Arrange
        User user = new User();
        user.setIdUser(1L);

        // Act
        userService.generatePasswordResetToken(user);

        // Assert
        assertNotNull(user.getResetPasswordToken());  // Verify that a token was generated
        assertNotNull(user.getResetPasswordTokenExpiry());  // Verify that an expiry date was set
        verify(userRepository, times(1)).save(user);  // Verify that the user was saved with the new token
    }

    // Test for resetting password with valid token
    @Test
    void testResetPassword_ValidToken() {
        // Arrange
        String token = "valid-token";
        User user = new User();
        user.setResetPasswordToken(token);
        user.setResetPasswordTokenExpiry(new Date(System.currentTimeMillis() + 3600000));  // 1 hour from now

        when(userRepository.findByResetPasswordToken(token)).thenReturn(user);

        // Act
        userService.resetPassword(token, "newPassword");

        // Assert
        assertEquals("newPassword", user.getPassword());  // Verify that the password was updated
        assertNull(user.getResetPasswordToken());  // Verify that the token was cleared
        assertNull(user.getResetPasswordTokenExpiry());  // Verify that the expiry was cleared
        verify(userRepository, times(1)).save(user);  // Verify that the user was saved with the updated password
    }

    // Test for verifying email with valid token
    @Test
    void testVerifyEmail_Success() {
        // Arrange
        String verificationToken = "valid-token";
        User user = new User();
        user.setVerificationToken(verificationToken);

        when(userRepository.findByVerificationToken(verificationToken)).thenReturn(user);

        // Act
        boolean result = userService.verifyEmail(verificationToken);

        // Assert
        assertTrue(result);  // Email verification should succeed
        assertTrue(user.isVerified());  // Verify that the user is marked as verified
        assertNull(user.getVerificationToken());  // Verify that the token was cleared
        verify(userRepository, times(1)).save(user);  // Verify that the user was saved
    }

    // Test for verifying email with invalid token
    @Test
    void testVerifyEmail_InvalidToken() {
        // Arrange
        String verificationToken = "invalid-token";

        when(userRepository.findByVerificationToken(verificationToken)).thenReturn(null);

        // Act
        boolean result = userService.verifyEmail(verificationToken);

        // Assert
        assertFalse(result);  // Email verification should fail
        verify(userRepository, times(0)).save(any(User.class));  // No save should occur
    }
}
