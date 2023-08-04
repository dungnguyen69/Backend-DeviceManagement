package com.fullstack.Backend.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.fullstack.Backend.entities.ItemType;
import com.fullstack.Backend.utils.dropdowns.ItemTypeList;

public interface ItemTypeService {
	public CompletableFuture<ItemType> findByName(String name);
	public CompletableFuture<Boolean> doesItemTypeExist(int id);

	public CompletableFuture<List<String>> getItemTypeList();

	public CompletableFuture<List<ItemTypeList>> fetchItemTypes();
}
