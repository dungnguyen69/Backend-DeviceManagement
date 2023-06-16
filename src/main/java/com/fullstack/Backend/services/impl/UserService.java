package com.fullstack.Backend.services.impl;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.fullstack.Backend.dto.users.LoginDTO;
import com.fullstack.Backend.dto.users.RegisterDTO;
import com.fullstack.Backend.entities.SystemRole;
import com.fullstack.Backend.enums.Role;
import com.fullstack.Backend.repositories.interfaces.ISystemRoleRepository;
import com.fullstack.Backend.responses.users.JwtResponse;
import com.fullstack.Backend.responses.users.MessageResponse;
import com.fullstack.Backend.security.JwtUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.fullstack.Backend.entities.User;
import com.fullstack.Backend.repositories.interfaces.IUserRepository;
import com.fullstack.Backend.services.IUserService;


@Service
public class UserService implements IUserService {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    IUserRepository _userRepository;

    @Autowired
    ISystemRoleRepository _systemRoleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private JavaMailSender mailSender;

    @Async
    @Override
    public CompletableFuture<User> findById(int id) {
        return CompletableFuture.completedFuture(_userRepository.findById(id).get());
    }


    @Override
    public CompletableFuture<Boolean> doesUserExist(int id) {
        return CompletableFuture.completedFuture(_userRepository.existsById((long) id));
    }

    @Async
    @Override
    public CompletableFuture<User> findByUsername(String username) {
        return CompletableFuture.completedFuture(_userRepository.findByUserName(username).get());
    }

    @Async
    @Override
    public CompletableFuture<List<User>> getUserList() {
        return CompletableFuture.completedFuture(_userRepository.findAll());
    }

    @Async
    @Override
    public CompletableFuture<ResponseEntity<Object>> authenticateUser(LoginDTO loginRequest, Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getUser().getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles)));
    }

    @Async
    @Override
    public CompletableFuture<ResponseEntity<Object>> registerUser(RegisterDTO registerRequest, String siteURL) throws MessagingException, UnsupportedEncodingException {
        if (_userRepository.existsByUserName(registerRequest.getUserName())) {
            return CompletableFuture.completedFuture(ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!")));
        }

        if (_userRepository.existsByEmail(registerRequest.getEmail())) {
            return CompletableFuture.completedFuture(ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!")));
        }

        // Create new user's account
        User user = new User();
        user.setUserName(registerRequest.getUserName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encoder.encode(registerRequest.getPassword()));
        String randomCode = RandomString.make(64);
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setProject(registerRequest.getProject());
        user.setVerificationCode(randomCode);
        user.setBadgeId(registerRequest.getBadgeId());
        user.setCreatedDate(new Date());
        user.setEnabled(false);
        Set<String> strRoles = registerRequest.getRole();
        Set<SystemRole> roles = new HashSet<>();
        if (strRoles == null) {
            SystemRole userRole = _systemRoleRepository.findByName(Role.USER.name())
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        SystemRole adminRole = _systemRoleRepository.findByName(Role.USER.name())
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                    }
                    case "mod" -> {
                        SystemRole modRole = _systemRoleRepository.findByName(Role.MODERATOR.name())
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                    }
                    default -> {
                        SystemRole userRole = _systemRoleRepository.findByName(Role.ADMIN.name())
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                    }
                }
            });
        }
        user.setSystemRoles(roles);
        _userRepository.save(user);
        sendVerificationEmail(user, siteURL);
        return CompletableFuture.completedFuture(ResponseEntity.ok(new MessageResponse("User registered successfully!")));
    }

    @Override
    public void sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "dungtestemail33@gmail.com";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        content = content.replace("[[name]]", user.getFirstName().concat(" " + user.getLastName()));
        String verifyURL = siteURL + "/api/users/verify?code=" + user.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);
        helper.setText(content, true);
        mailSender.send(message);
    }

    @Async
    @Override
    public CompletableFuture<ResponseEntity<Object>> verify(String verificationCode) {
        User user = _userRepository.findByVerificationCode(verificationCode);
        if (user == null || user.isEnabled())
            return CompletableFuture.completedFuture(ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Sorry, we could not verify account. It maybe already verified,\n" +
                            "        or verification code is incorrect.")));
        user.setVerificationCode(null);
        user.setEnabled(true);
        _userRepository.save(user);
        return CompletableFuture.completedFuture(ResponseEntity
                .ok()
                .body(new MessageResponse("Verify successfully")));

    }
}
