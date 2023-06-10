package com.fullstack.Backend.services.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fullstack.Backend.entities.Storage;
import com.fullstack.Backend.repositories.interfaces.IStorageRepository;
import com.fullstack.Backend.services.IStorageService;
import com.fullstack.Backend.utils.dropdowns.StorageList;

@Service
public class StorageService implements IStorageService {

	@Autowired
	IStorageRepository _storageRepository;
	@Async
	@Override
	public CompletableFuture<Storage> findBySize(int size) {
		return CompletableFuture.completedFuture(_storageRepository.findBySize(size));
	}

	@Override
	public CompletableFuture<Boolean> doesStorageExist(int id) {
		return CompletableFuture.completedFuture(_storageRepository.existsById((long) id));
	}

	@Async
	@Override
	public CompletableFuture<List<String>> getStorageList() {
		return CompletableFuture.completedFuture(_storageRepository.findStorageSize());
	}
	@Override
	public CompletableFuture<List<StorageList>> fetchStorage() {
		return CompletableFuture.completedFuture(_storageRepository.fetchStorage());
	}
}
