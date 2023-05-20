package com.fullstack.Backend.repositories.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fullstack.Backend.entities.Ram;

public interface IRamRepository extends JpaRepository<Ram, Long> {
	public static final String FIND_RAM_SIZES = "select size from rams";
	public static final String FIND_RAM = "select * from rams WHERE size = ?";

	@Query(value = FIND_RAM_SIZES, nativeQuery = true)
	public List<String> findRamSize();

	@Query(value = FIND_RAM, nativeQuery = true)
	public Ram findBySize(int size);
}
