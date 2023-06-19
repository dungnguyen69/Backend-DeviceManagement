package com.fullstack.Backend.repositories.interfaces;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.fullstack.Backend.entities.Ram;
import com.fullstack.Backend.utils.dropdowns.RamList;

public interface IRamRepository extends JpaRepository<Ram, Long> {
	public static final String FIND_RAM_SIZES = "SELECT size FROM Ram";
	public static final String FIND_RAM = "SELECT r FROM Ram r WHERE size = :size";
	public static final String FETCH_RAMS = "SELECT Id, size FROM Ram";
	
	@Query(FIND_RAM_SIZES)
	public List<String> findRamSize();

	@Query(FIND_RAM)
	public Ram findBySize(int size);
	
	@Query(FETCH_RAMS)
	public List<RamList> fetchRams();
}
