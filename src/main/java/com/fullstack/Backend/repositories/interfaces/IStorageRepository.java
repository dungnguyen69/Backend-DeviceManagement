package com.fullstack.Backend.repositories.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fullstack.Backend.entities.Storage;

public interface IStorageRepository extends JpaRepository<Storage, Long> {
	public static final String FIND_STORAGE_SIZES = "select size from storages";
	public static final String FIND_STORAGE = "select * from storages WHERE size = ?";

	@Query(value = FIND_STORAGE_SIZES, nativeQuery = true)
	public List<String> findStorageSize();
	
	@Query(value = FIND_STORAGE, nativeQuery = true)
	public Storage findBySize(int size);
}
