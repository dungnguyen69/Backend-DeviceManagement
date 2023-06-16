package com.fullstack.Backend.repositories.interfaces;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fullstack.Backend.entities.User;
import org.springframework.data.jpa.repository.Query;

public interface IUserRepository extends JpaRepository<User, Long> {
    public final static String FIND_BY_VERIFICATION_CODE = "SELECT u FROM User u WHERE u.verificationCode = :code";
    public Optional<User> findById(int id);

    public Optional<User> findByUserName(String userName);

    Boolean existsByUserName(String userName);

    Boolean existsByEmail(String email);

    @Query(FIND_BY_VERIFICATION_CODE)
    public User findByVerificationCode(String code);
}
