package com.fullstack.Backend.repositories.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fullstack.Backend.entities.ItemType;

public interface IItemTypeRepository extends JpaRepository<ItemType, Long> {

	public static final String FIND_ITEM_TYPES = "select name from item_types";

	@Query(value = FIND_ITEM_TYPES, nativeQuery = true)
	public List<String> findItemtypeNames();

	@Query(value = "SELECT * FROM item_types it WHERE name = ?", nativeQuery = true)
	public ItemType findByName(String name);
}
