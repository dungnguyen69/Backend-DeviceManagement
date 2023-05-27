package com.fullstack.Backend.repositories.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fullstack.Backend.entities.ItemType;
import com.fullstack.Backend.utils.dropdowns.ItemTypeList;

public interface IItemTypeRepository extends JpaRepository<ItemType, Long> {

	public static final String FIND_ITEM_TYPE_NAMES = "SELECT name FROM item_types";
	public static final String FETCH_ITEM_TYPES = "SELECT id, name FROM item_types";

	@Query(value = FIND_ITEM_TYPE_NAMES, nativeQuery = true)
	public List<String> findItemtypeNames();

	@Query(value = "SELECT * FROM item_types it WHERE name = ?", nativeQuery = true)
	public ItemType findByName(String name);

	@Query(value = FETCH_ITEM_TYPES, nativeQuery = true)
	public List<ItemTypeList> fetchItemTypes();
}
