package com.fullstack.Backend.repositories.interfaces;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.fullstack.Backend.entities.Storage;
import com.fullstack.Backend.utils.dropdowns.StorageList;
import org.springframework.stereotype.Repository;

@Repository

public interface StorageRepository extends JpaRepository<Storage, Long> {
	public static final String FIND_STORAGE_SIZES = "SELECT size FROM Storage";
	public static final String FIND_STORAGE = "SELECT s FROM Storage s WHERE size = :size";
	public static final String FETCH_STORAGE = "SELECT s FROM Storage s";

	@Query(FIND_STORAGE_SIZES)
	public List<String> findStorageSize();
	
	@Query(FIND_STORAGE)
	public Storage findBySize(String size);
	
	@Query(value = FETCH_STORAGE)
	public List<StorageList> fetchStorage();
}
