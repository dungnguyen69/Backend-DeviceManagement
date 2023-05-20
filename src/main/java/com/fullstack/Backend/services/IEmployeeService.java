package com.fullstack.Backend.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.fullstack.Backend.entities.User;

public interface IEmployeeService {
	public CompletableFuture<User> findById(int id);

	public CompletableFuture<User> findByUsername(String username);

	public CompletableFuture<List<User>> getUserList();

}
