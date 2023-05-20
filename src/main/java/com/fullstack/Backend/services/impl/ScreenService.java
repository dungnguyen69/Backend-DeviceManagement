package com.fullstack.Backend.services.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fullstack.Backend.entities.Screen;
import com.fullstack.Backend.repositories.interfaces.IScreenRepository;
import com.fullstack.Backend.services.IScreenService;

@Service
public class ScreenService implements IScreenService {

	@Autowired
	IScreenRepository _screenRepository;

	@Async
	@Override
	public CompletableFuture<Screen> findBySize(int size) {
		return CompletableFuture.completedFuture(_screenRepository.findBySize(size));
	}

	@Async
	@Override
	public CompletableFuture<List<String>> getScreenList() {
		return CompletableFuture.completedFuture(_screenRepository.findScreenSize());
	}

}
