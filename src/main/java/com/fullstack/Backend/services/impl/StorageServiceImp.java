package com.fullstack.Backend.services.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fullstack.Backend.entities.Storage;
import com.fullstack.Backend.repositories.interfaces.StorageRepository;
import com.fullstack.Backend.services.StorageService;
import com.fullstack.Backend.utils.dropdowns.StorageList;

@Service
@CacheConfig(cacheNames = {"storage"})
public class StorageServiceImp implements StorageService {

	@Autowired
    StorageRepository _storageRepository;

	@Async
	@Override
	@Cacheable(key = "size")
	public CompletableFuture<Storage> findBySize(String size) {
		return CompletableFuture.completedFuture(_storageRepository.findBySize(size));
	}

	@Async
	@Override
	public CompletableFuture<Boolean> doesStorageExist(int id) {
		return CompletableFuture.completedFuture(_storageRepository.existsById((long) id));
	}

	@Async
	@Override
	public CompletableFuture<List<String>> getStorageList() {
		return CompletableFuture.completedFuture(_storageRepository.findStorageSize());
	}

	@Async
	@Override
	public CompletableFuture<List<StorageList>> fetchStorage() {
		return CompletableFuture.completedFuture(_storageRepository.fetchStorage());
	}
}
