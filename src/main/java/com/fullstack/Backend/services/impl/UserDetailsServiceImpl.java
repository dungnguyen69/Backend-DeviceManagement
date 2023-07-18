package com.fullstack.Backend.services.impl;

import com.fullstack.Backend.entities.User;
import com.fullstack.Backend.repositories.interfaces.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/* Returns a UserDetails object that Spring Security can use for authentication and validation.
 *  UserDetails contains necessary information (such as: username, password, authorities) to build an Authentication object.
 *
 * */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserService _userService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CompletableFuture<User> user = _userService.findByUsername(username);
        try {
            return UserDetailsImpl.build(user.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
