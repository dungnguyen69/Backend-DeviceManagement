package com.fullstack.Backend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fullstack.Backend.entities.User;
import com.fullstack.Backend.repositories.interfaces.IEmployeeRepository;
import com.fullstack.Backend.services.IEmployeeService;

@Service
public class EmployeeService implements IEmployeeService {

	@Autowired
	IEmployeeRepository _employeeRepository;

	@Override
	public User findById(int id) {
		return _employeeRepository.findById(id).get();
	}

	@Override
	public User findByUsername(String username) {
		return _employeeRepository.findByUserName(username);
	}
}
