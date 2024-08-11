package com.greenhouse.gh_backend.restController;

import com.greenhouse.gh_backend.entities.Role;
import com.greenhouse.gh_backend.entities.User;
import com.greenhouse.gh_backend.payload.request.LoginRequest;
import com.greenhouse.gh_backend.payload.request.SignupRequest;
import com.greenhouse.gh_backend.payload.response.JwtResponse;
import com.greenhouse.gh_backend.payload.response.MessageResponse;
import com.greenhouse.gh_backend.repositories.UserRepository;
import com.greenhouse.gh_backend.security.CodeGenerator;
import com.greenhouse.gh_backend.security.jwt.JwtUtils;
import com.greenhouse.gh_backend.security.services.UserDetailsImpl;
import com.greenhouse.gh_backend.services.EmailSenderService;
import com.greenhouse.gh_backend.services.IUser;
import com.greenhouse.gh_backend.services.UserServiceIMP;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
@CrossOrigin(origins = {"*", "https://b94d-197-27-101-27.ngrok-free.app"}, maxAge = 3600, allowCredentials = "true")

//@CrossOrigin(origins = {"http://localhost:4200", "https://b94d-197-27-101-27.ngrok-free.app"}, maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IUser iUser;
    @Autowired
    private UserServiceIMP userService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private EmailSenderService emailService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Role role = userDetails.getUser().getRole();

        boolean profileComplete = userDetails.getUser().isProfileComplete();

        JwtResponse jwtResponse = new JwtResponse(
                jwt,
                userDetails.getIdUser(),
                userDetails.getEmail(),
                userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.getCompanyName(),
                userDetails.getSector(),
                userDetails.getIndustry(),
                Collections.singletonList(role.name()),
                userDetails.getRevenue(),
                userDetails.getHeadquarters(),
                userDetails.getCurrency(),
                profileComplete,
                userDetails.getBaseYear(), // Include baseYear
                userDetails.getEndYear(), // Include endYear
                userDetails.getReductionAmbition() // Include reductionAmbition
        );

        return ResponseEntity.ok(jwtResponse);

    }

        @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @ModelAttribute SignupRequest signUpRequest) throws ParseException {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }
            if (!userService.isCompanyEmail(signUpRequest.getEmail())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Email must be a company email!"));
            }
        String verificationToken = UUID.randomUUID().toString();
            String verificationCode = CodeGenerator.generateVerificationCode();

            String encodedPassword = encoder.encode(signUpRequest.getPassword());

        User user = new User(signUpRequest.getFirstName(), signUpRequest.getLastName(), signUpRequest.getEmail(),signUpRequest.getCompanyName(), encodedPassword);
        user.setRole(Role.COMPANY);
        user.setVerificationToken(verificationToken);
        userRepository.save(user);

            user.setVerificationCode(verificationCode);
            user.setVerificationCodeExpiry(new Date(System.currentTimeMillis() + 3600 * 1000)); // 1 hour expiry
            userRepository.save(user);

            CompletableFuture.runAsync(() -> emailSenderService.sendVerificationCode(user, verificationCode));

            return ResponseEntity.ok(new MessageResponse("User registered successfully! Please check your email for verification."));
    }
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");

        // Log the received data for debugging
        System.out.println("Received email: " + email);
        System.out.println("Received code: " + code);

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null || user.isVerified()) {
            return ResponseEntity.badRequest().body("Invalid email or user already verified");
        }

        if (user.getVerificationCode().equals(code) && user.getVerificationCodeExpiry().after(new Date())) {
            user.setVerified(true);
            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("Email verified successfully!"));
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired verification code");
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true)
                .maxAge(0)
                .path("/")
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(new MessageResponse("Logged out successfully"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        System.out.println("Received email: " + email);
        User user = userService.findByEmail(email);
        if (user != null) {
            userService.generatePasswordResetToken(user);
            emailService.sendPasswordResetEmail(user);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }

    @PostMapping("/reset-password")
    public void resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        String encodedPassword = encoder.encode(newPassword);
        userService.resetPassword(token, encodedPassword);
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String verificationToken) {
        boolean emailVerified = userService.verifyEmail(verificationToken);

        if (emailVerified) {
            return "Email verified successfully!";
        } else {
            return "Invalid verification token!";
        }
    }

    @GetMapping("/id")
    public ResponseEntity<Long> getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Long userId = userDetails.getIdUser();
            return ResponseEntity.ok(userId);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/refresh_token")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String refreshToken = bearerToken.substring(7);
            // Process the token to generate a new one
            // Ensure you handle any exceptions such as an invalid token
        } else {
            return ResponseEntity.badRequest().body("Bearer token is missing or improperly formatted");
        }

        return ResponseEntity.ok().body("New access token");
    }
    @GetMapping("/getUserById/{id}")
    public User retrieveById(@PathVariable Long id) {
        return iUser.retrieveById(id);
    }
    // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllUsers")
    public List<User> retrieveAllUsers() {
        return iUser.retrieveAllUsers();
    }
    //  @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateUser/{id}")
    public ResponseEntity<?> updateUser(@RequestBody User updateUser, @PathVariable Long id) {
        try {
            iUser.updateUser(id, updateUser);
            return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("An error occurred while updating user"));

        }
    }

    @DeleteMapping("/deleteUser/{id}")
    public void deleteById(@PathVariable Long id) {
        try {
            System.out.println("Received user ID: " + id);
            iUser.deleteUser(id); // Assuming iUser is an instance of a service class
            System.out.println("User deleted successfully");
        } catch (Exception e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }
    @GetMapping("/{userId}/profile-complete")
    public ResponseEntity<Boolean> isProfileComplete(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.isProfileComplete(userId));
    }

    @PostMapping("/{userId}/complete-profile")
    public ResponseEntity<User> completeProfile(@PathVariable Long userId, @RequestBody User updateUser) {
        User updatedUser = userService.completeProfile(userId, updateUser);
        return ResponseEntity.ok(updatedUser);
    }
}
