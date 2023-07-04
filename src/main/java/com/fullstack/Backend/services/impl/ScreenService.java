package com.fullstack.Backend.services.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fullstack.Backend.entities.Screen;
import com.fullstack.Backend.repositories.interfaces.IScreenRepository;
import com.fullstack.Backend.services.IScreenService;
import com.fullstack.Backend.utils.dropdowns.ScreenList;

@Service
public class ScreenService implements IScreenService {

	@Autowired
	IScreenRepository _screenRepository;

	@Async
	@Override
	public CompletableFuture<Screen> findBySize(String size) {
		return CompletableFuture.completedFuture(_screenRepository.findBySize(size));
	}

	@Override
	public CompletableFuture<Boolean> doesScreenExist(int id) {
		return CompletableFuture.completedFuture(_screenRepository.existsById((long) id));
	}

	@Async
	@Override
	public CompletableFuture<List<String>> getScreenList() {
		return CompletableFuture.completedFuture(_screenRepository.findScreenSize());
	}

	@Override
	public CompletableFuture<List<ScreenList>> fetchScreen() {
		return CompletableFuture.completedFuture(_screenRepository.fetchScreen());
	}

}
