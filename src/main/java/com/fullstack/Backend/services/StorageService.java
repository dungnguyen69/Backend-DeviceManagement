package com.fullstack.Backend.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import com.fullstack.Backend.entities.Storage;
import com.fullstack.Backend.utils.dropdowns.StorageList;

public interface StorageService {
	public CompletableFuture<Storage> findBySize(String size);
	public CompletableFuture<Boolean> doesStorageExist(int id);

	public CompletableFuture<List<String>> getStorageList();
	
	public CompletableFuture<List<StorageList>> fetchStorage();

}
