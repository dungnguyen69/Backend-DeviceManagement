package com.fullstack.Backend.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.fullstack.Backend.entities.Screen;

public interface IScreenService {
	public CompletableFuture<Screen> findBySize(int size);

	public CompletableFuture<List<String>> getScreenList();

}
