package com.greenhouse.gh_backend.entities;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUser;

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be less than 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be less than 50 characters")
    private String lastName;

    @Size(max = 100, message = "Company name must be less than 100 characters")
    private String companyName;

    private String sector;
    private String industry;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must be less than 100 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must be at least 8 characters long, contain at least one uppercase letter, one number, and one special character")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
    private String resetPasswordToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date resetPasswordTokenExpiry;

    private boolean verified;
    private String verificationToken;
    private Float revenue;
    private String headquarters;
    private String currency;
    private boolean profileComplete = false;
    private String verificationCode;
    private Date verificationCodeExpiry;
    private double totalFootprint;
    private int baseYear;
    private int endYear;
    private double reductionAmbition;
    private double targetReduction;

    public User() {}

    public User(String firstName, String lastName, String email, String companyName, String encodedPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.companyName = companyName;
        this.password = encodedPassword;
        this.verified = false; // Default to false until verified
    }

    @OneToMany(mappedBy = "user")
    private List<Location> locations;

    @OneToMany(mappedBy = "user")
    private List<Vehicle> vehicles;
    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
