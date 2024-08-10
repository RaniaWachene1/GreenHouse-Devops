package com.greenhouse.gh_backend.services;

import com.greenhouse.gh_backend.entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.List;

public interface IUser  {


    User addUser(User user);

    User retrieveById(Long id);


    void updateUser(Long userId, User updateUser);

    List<User> retrieveAllUsers();
    void deleteUser(Long id);

    User findByEmail(String email);



    boolean verifyEmail(String verificationToken);




}

