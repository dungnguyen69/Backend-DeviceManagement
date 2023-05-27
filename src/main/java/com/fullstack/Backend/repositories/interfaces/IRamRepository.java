package com.fullstack.Backend.repositories.interfaces;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.fullstack.Backend.entities.Ram;
import com.fullstack.Backend.utils.dropdowns.RamList;

public interface IRamRepository extends JpaRepository<Ram, Long> {
	public static final String FIND_RAM_SIZES = "SELECT size FROM rams";
	public static final String FIND_RAM = "SELECT * FROM rams WHERE size = ?";
	public static final String FETCH_RAMS = "SELECT id, size FROM rams";
	
	@Query(value = FIND_RAM_SIZES, nativeQuery = true)
	public List<String> findRamSize();

	@Query(value = FIND_RAM, nativeQuery = true)
	public Ram findBySize(int size);
	
	@Query(value = FETCH_RAMS, nativeQuery = true)
	public List<RamList> fetchRams();
}
