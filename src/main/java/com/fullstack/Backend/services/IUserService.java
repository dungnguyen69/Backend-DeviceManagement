package com.fullstack.Backend.services;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.fullstack.Backend.dto.users.LoginDTO;
import com.fullstack.Backend.dto.users.RegisterDTO;
import com.fullstack.Backend.entities.User;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface IUserService {
    public CompletableFuture<User> findById(int id);

    public CompletableFuture<Boolean> doesUserExist(int id);

    public CompletableFuture<User> findByUsername(String username);

    public CompletableFuture<List<User>> getUserList();

    public CompletableFuture<ResponseEntity<Object>> authenticateUser(LoginDTO loginRequest, Authentication authentication);

    public CompletableFuture<ResponseEntity<Object>> registerUser(RegisterDTO registerRequest, String siteURL) throws MessagingException, UnsupportedEncodingException;

    public void sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException;

    public CompletableFuture<ResponseEntity<Object>> verify(String verificationCode);

}
