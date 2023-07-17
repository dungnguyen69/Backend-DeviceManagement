package com.fullstack.Backend.repositories.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fullstack.Backend.entities.ItemType;
import com.fullstack.Backend.utils.dropdowns.ItemTypeList;

public interface ItemTypeRepository extends JpaRepository<ItemType, Long> {

	public static final String FIND_ITEM_TYPE_NAMES = "SELECT name FROM ItemType";
	public static final String FETCH_ITEM_TYPES = "SELECT it FROM ItemType it";
	public static final String FIND_BY_NAME = "SELECT it FROM ItemType it WHERE name = :name";
	@Query(FIND_ITEM_TYPE_NAMES)
	public List<String> findItemTypeNames();

	@Query(FIND_BY_NAME)
	public ItemType findByName(String name);

	@Query(FETCH_ITEM_TYPES)
	public List<ItemTypeList> fetchItemTypes();
}
