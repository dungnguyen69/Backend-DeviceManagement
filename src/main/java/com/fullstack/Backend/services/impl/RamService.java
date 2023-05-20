package com.fullstack.Backend.services.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fullstack.Backend.entities.Ram;
import com.fullstack.Backend.repositories.interfaces.IRamRepository;
import com.fullstack.Backend.services.IRamService;

@Service
public class RamService implements IRamService {

	@Autowired
	IRamRepository _ramRepository;
	
	@Async
	@Override
	public CompletableFuture<Ram> findBySize(int size) {
		return CompletableFuture.completedFuture(_ramRepository.findBySize(size));
	}

	@Async
	@Override
	public CompletableFuture<List<String>> getRamList() {
		return CompletableFuture.completedFuture(_ramRepository.findRamSize());
	}

}
