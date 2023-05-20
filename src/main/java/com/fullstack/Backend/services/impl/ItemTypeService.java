package com.fullstack.Backend.services.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fullstack.Backend.entities.ItemType;
import com.fullstack.Backend.repositories.interfaces.IItemTypeRepository;
import com.fullstack.Backend.services.IItemTypeService;

@Service
public class ItemTypeService implements IItemTypeService {
	@Autowired
	IItemTypeRepository _itemTypeRepository;

	@Async
	@Override
	public CompletableFuture<ItemType> findByName(String name) {
		return CompletableFuture.completedFuture(_itemTypeRepository.findByName(name));
	}

	@Async
	@Override
	public CompletableFuture<List<String>> getItemTypeList() {
		return CompletableFuture.completedFuture(_itemTypeRepository.findItemtypeNames());
	}

}
