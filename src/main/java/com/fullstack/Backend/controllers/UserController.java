package com.fullstack.Backend.controllers;

import com.fullstack.Backend.dto.users.FilterUserDTO;
import com.fullstack.Backend.dto.users.LoginDTO;
import com.fullstack.Backend.dto.users.RegisterDTO;
import com.fullstack.Backend.services.IUserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.fullstack.Backend.constant.constant.*;
import static com.fullstack.Backend.constant.constant.DEFAULT_SORT_DIRECTION;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    IUserService _userService;

    @PostMapping("/login")
    public CompletableFuture<ResponseEntity<Object>> authenticateUser(@Valid @RequestBody LoginDTO loginRequest) {
        /* If the authentication process is successful,
        we can get User’s information such as username, password,
            authorities from an Authentication object. */
        /* gets {username, password} from login Request, AuthenticationManager will use it to authenticate a login account */

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        return _userService.authenticateUser(loginRequest, authentication);
    }

    @PostMapping("/register")
    public CompletableFuture<ResponseEntity<Object>> registerUser(@Valid @RequestBody RegisterDTO registerRequest, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        return _userService.registerUser(registerRequest, getSiteURL(request));
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @GetMapping("/verify")
    public CompletableFuture<ResponseEntity<Object>> verifyUser(@Param("code") String code) throws ExecutionException, InterruptedException {
        return _userService.verify(code);
    }

    @GetMapping()
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<Object>> getUsers(
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            FilterUserDTO dto) throws ExecutionException, InterruptedException {
        return _userService.showUsersWithPaging(pageNo, pageSize, sortBy, sortDir, dto);
    }
}
