package com.fullstack.Backend.services;

import com.fullstack.Backend.entities.User;

public interface IEmployeeService {
	public User findById(int id);
	public User findByUsername(String username);
}
