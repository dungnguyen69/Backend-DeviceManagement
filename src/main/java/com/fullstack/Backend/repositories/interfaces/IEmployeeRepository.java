package com.fullstack.Backend.repositories.interfaces;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fullstack.Backend.entities.User;

public interface IEmployeeRepository	extends JpaRepository<User, Long> {
	public Optional<User> findById(int id);
	public User findByUserName(String username);
}
