package com.fullstack.Backend.services.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fullstack.Backend.entities.User;
import com.fullstack.Backend.repositories.interfaces.IEmployeeRepository;
import com.fullstack.Backend.services.IEmployeeService;

@Service
public class EmployeeService implements IEmployeeService {

    @Autowired
    IEmployeeRepository _employeeRepository;

    @Async
    @Override
    public CompletableFuture<User> findById(int id) {
        return CompletableFuture.completedFuture(_employeeRepository.findById(id).get());
    }

    @Override
    public CompletableFuture<Boolean> doesUserExist(int id) {
        return CompletableFuture.completedFuture(_employeeRepository.existsById((long) id));
    }

    @Async
    @Override
    public CompletableFuture<User> findByUsername(String username) {
        return CompletableFuture.completedFuture(_employeeRepository.findByUserName(username));
    }

    @Async
    @Override
    public CompletableFuture<List<User>> getUserList() {
        return CompletableFuture.completedFuture(_employeeRepository.findAll());
    }
}
