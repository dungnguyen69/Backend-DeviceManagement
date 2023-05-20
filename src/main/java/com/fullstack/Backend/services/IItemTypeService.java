package com.fullstack.Backend.services;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.fullstack.Backend.entities.ItemType;

public interface IItemTypeService {
	public CompletableFuture<ItemType> findByName(String name);

	public CompletableFuture<List<String>> getItemTypeList();

}
