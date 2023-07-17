package com.fullstack.Backend.services.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fullstack.Backend.entities.ItemType;
import com.fullstack.Backend.repositories.interfaces.ItemTypeRepository;
import com.fullstack.Backend.services.IItemTypeService;
import com.fullstack.Backend.utils.dropdowns.ItemTypeList;

@Service
public class ItemTypeService implements IItemTypeService {
	@Autowired
    ItemTypeRepository _itemTypeRepository;

	@Async
	@Override
	public CompletableFuture<ItemType> findByName(String name) {
		return CompletableFuture.completedFuture(_itemTypeRepository.findByName(name));
	}

	@Override
	public CompletableFuture<Boolean> doesItemTypeExist(int id) {
		return CompletableFuture.completedFuture(_itemTypeRepository.existsById((long) id));
	}

	@Async
	@Override
	public CompletableFuture<List<String>> getItemTypeList() {
		return CompletableFuture.completedFuture(_itemTypeRepository.findItemTypeNames());
	}

	@Override
	public CompletableFuture<List<ItemTypeList>> fetchItemTypes() {
		return CompletableFuture.completedFuture(_itemTypeRepository.fetchItemTypes());
	}

}
