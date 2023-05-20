package com.fullstack.Backend.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.fullstack.Backend.entities.Storage;

public interface IStorageService {
	public CompletableFuture<Storage> findBySize(int size);

	public CompletableFuture<List<String>> getStorageList();

}
