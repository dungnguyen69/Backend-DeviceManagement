package com.fullstack.Backend.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.fullstack.Backend.entities.Platform;

public interface IPlatformService {
	public CompletableFuture<List<String>> getPlatformNameList();

	public CompletableFuture<List<String>> getPlatformVersionList();

	public CompletableFuture<List<String>> getPlatformNameVersionList();

	public CompletableFuture<Platform> findByNameAndVersion(String name, String version);
}
