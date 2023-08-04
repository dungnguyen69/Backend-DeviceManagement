package com.fullstack.Backend.services.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fullstack.Backend.entities.Platform;
import com.fullstack.Backend.repositories.interfaces.PlatformRepository;
import com.fullstack.Backend.services.PlatformService;
import com.fullstack.Backend.utils.dropdowns.PlatformList;

@Service
@CacheConfig(cacheNames = {"platform"})
public class PlatformServiceImp implements PlatformService {

	@Autowired
    PlatformRepository _platformRepository;

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
	public CompletableFuture<Boolean> doesPlatformExist(int id) {
		return CompletableFuture.completedFuture(_platformRepository.existsById((long) id));
	}

	@Async
	@Override
	@Cacheable(key="{#name, #version}")
	public CompletableFuture<Platform> findByNameAndVersion(String name, String version) {
		return CompletableFuture.completedFuture(_platformRepository.findByNameAndVersion(name, version));
	}

	@Async
	@Override
	public CompletableFuture<List<PlatformList>> fetchPlatform() {
		return CompletableFuture.completedFuture(_platformRepository.fetchPlatform());
	}

}
