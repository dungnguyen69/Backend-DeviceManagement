package com.fullstack.Backend.services;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.fullstack.Backend.dto.users.*;
import com.fullstack.Backend.entities.PasswordResetToken;
import com.fullstack.Backend.entities.User;
import com.fullstack.Backend.entities.VerificationToken;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface IUserService {
    public CompletableFuture<User> findById(int id);

    public CompletableFuture<Boolean> doesUserExist(int id);

    public CompletableFuture<User> findByUsername(String username);

    public CompletableFuture<List<UserDTO>> getUserList(FilterUserDTO dto) throws ExecutionException, InterruptedException;

    public void formatFilter(FilterUserDTO dto);

    public CompletableFuture<ResponseEntity<Object>> authenticateUser(LoginDTO loginRequest, Authentication
            authentication);

    public CompletableFuture<ResponseEntity<Object>> registerUser(RegisterDTO registerRequest, String siteURL) throws
            MessagingException, UnsupportedEncodingException;

    public void sendVerificationEmail(User user, String siteURL) throws
            MessagingException, UnsupportedEncodingException;

    public CompletableFuture<ResponseEntity<Object>> verify(String verificationCode) throws ExecutionException, InterruptedException;

    CompletableFuture<List<User>> fetchFilteredUsers(FilterUserDTO dto, List<User> users);

    public CompletableFuture<List<UserDTO>> getPage(List<UserDTO> sourceList, int pageIndex, int pageSize);

    public int getTotalPages(int pageSize, int listSize);

    public CompletableFuture<ResponseEntity<Object>> showUsersWithPaging(int pageIndex, int pageSize, String
            sortBy, String sortDir, FilterUserDTO dto) throws ExecutionException, InterruptedException;

    public void createVerificationToken(User user, String token);

    public CompletableFuture<VerificationToken> getVerificationToken(String VerificationToken);

    public CompletableFuture<PasswordResetToken> getResetPasswordToken(String token);

    public CompletableFuture<VerificationToken> generateNewVerificationToken(final String existingVerificationToken) throws ExecutionException, InterruptedException;

    public CompletableFuture<PasswordResetToken> generateResetPasswordToken(String existingToken) throws ExecutionException, InterruptedException;

    public CompletableFuture<ResponseEntity<Object>> resendRegistrationToken(String siteURL, String existingToken) throws ExecutionException, InterruptedException, MessagingException;

    public void resendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException;

    public CompletableFuture<ResponseEntity<Object>> resetPassword(String siteURL, String userEmail) throws ExecutionException, InterruptedException, MessagingException;

    public CompletableFuture<User> findByEmail(String email);

    public CompletableFuture<PasswordResetToken> findUserFromResetPasswordToken(User user);

    public void createPasswordResetTokenForUser(User user, String token);

    public void sendResetPasswordEmail(User user, String verifyURL) throws MessagingException;

    public CompletableFuture<ResponseEntity<Object>> changePassword(PasswordDTO dto) throws ExecutionException, InterruptedException, MessagingException;

    public CompletableFuture<String> validatePasswordResetToken(String token);

    public CompletableFuture<User> findByToken(String token);


}
