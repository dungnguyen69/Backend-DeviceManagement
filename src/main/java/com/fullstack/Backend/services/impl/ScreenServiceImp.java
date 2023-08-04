package com.fullstack.Backend.services.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fullstack.Backend.entities.Screen;
import com.fullstack.Backend.repositories.interfaces.ScreenRepository;
import com.fullstack.Backend.services.ScreenService;
import com.fullstack.Backend.utils.dropdowns.ScreenList;

@Service
@CacheConfig(cacheNames = {"screen"})
public class ScreenServiceImp implements ScreenService {

	@Autowired
    ScreenRepository _screenRepository;

	@Async
	@Override
	@Cacheable(key = "size")
	public CompletableFuture<Screen> findBySize(String size) {
		return CompletableFuture.completedFuture(_screenRepository.findBySize(size));
	}

	@Async
	@Override
	public CompletableFuture<Boolean> doesScreenExist(int id) {
		return CompletableFuture.completedFuture(_screenRepository.existsById((long) id));
	}

	@Async
	@Override
	public CompletableFuture<List<String>> getScreenList() {
		return CompletableFuture.completedFuture(_screenRepository.findScreenSize());
	}

	@Async
	@Override
	public CompletableFuture<List<ScreenList>> fetchScreen() {
		return CompletableFuture.completedFuture(_screenRepository.fetchScreen());
	}

}
