package com.fullstack.Backend.repositories.interfaces;

import java.util.List;
import java.util.Optional;

import com.fullstack.Backend.utils.dropdowns.UserList;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fullstack.Backend.entities.User;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    //    public final static String FIND_BY_VERIFICATION_CODE = "SELECT u FROM User u WHERE u.verificationCode = :code";
    public final static String FETCH_USER = "SELECT u FROM User u";

    public User findById(int id);

    @Query(FETCH_USER)
    public List<UserList> fetchUser();

    public Optional<User> findByUserName(String userName);

    public User findByEmail(String email);

    Boolean existsByUserName(String userName);

    Boolean existsByEmail(String email);

//    @Query(FIND_BY_VERIFICATION_CODE)
//    public User findByVerificationCode(String code);
}
