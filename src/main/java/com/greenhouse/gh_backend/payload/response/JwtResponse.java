package com.greenhouse.gh_backend.payload.response;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long idUser;
    private String email;
    private String firstName;
    private String lastName;
    private String companyName;
    private String sector;
    private String industry;
    private List<String> roles;
    private Float revenue;
    private String headquarters;
    private String currency;
    private boolean profileComplete;
    private int baseYear; // Added field
    private int endYear; // Added field
    private double reductionAmbition; // Added field

    public JwtResponse(String token, Long idUser, String email, String firstName, String lastName, String companyName, String sector, String industry, List<String> roles, Float revenue, String headquarters, String currency, boolean profileComplete, int baseYear, int endYear, double reductionAmbition) {
        this.token = token;
        this.idUser = idUser;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
        this.sector = sector;
        this.industry = industry;
        this.roles = roles;
        this.revenue = revenue;
        this.headquarters = headquarters;
        this.currency = currency;
        this.profileComplete = profileComplete;
        this.baseYear = baseYear; // Initialize field
        this.endYear = endYear; // Initialize field
        this.reductionAmbition = reductionAmbition; // Initialize field
    }

    public JwtResponse(String newAccessToken, String refreshToken) {
        this.token = newAccessToken;
    }

    // Getters and setters are automatically provided by Lombok
}
