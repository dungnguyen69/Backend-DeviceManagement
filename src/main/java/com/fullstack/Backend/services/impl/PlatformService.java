package com.fullstack.Backend.services.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fullstack.Backend.entities.Platform;
import com.fullstack.Backend.repositories.interfaces.IPlatformRepository;
import com.fullstack.Backend.services.IPlatformService;

@Service
public class PlatformService implements IPlatformService {

	@Autowired
	IPlatformRepository _platformRepository;

	@Async
	@Override
	public CompletableFuture<List<String>> getPlatformNameList() {
		return CompletableFuture.completedFuture(_platformRepository.findPlatformName());
	}
	@Async
	@Override
	public CompletableFuture<List<String>> getPlatformVersionList() {
		return CompletableFuture.completedFuture(_platformRepository.findPlatformVersion());
	}
	@Async
	@Override
	public CompletableFuture<List<String>> getPlatformNameVersionList() {
		return CompletableFuture.completedFuture(_platformRepository.findPlatformNameVersion());
	}
	@Async
	@Override
	public CompletableFuture<Platform> findByNameAndVersion(String name, String version) {
		return CompletableFuture.completedFuture(_platformRepository.findByNameAndVersion(name, version));
	}

}
