package com.greenhouse.gh_backend.payload.response;

import java.util.List;

public class UserInfoResponse {
    private long idUser;
    private String email;
    private List<String> roles;

    public UserInfoResponse(long idUser,String email, List<String> roles) {
        this.idUser = idUser;
       this.email = email;
        this.roles = roles;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public List<String> getRoles() {
        return roles;
    }
}
