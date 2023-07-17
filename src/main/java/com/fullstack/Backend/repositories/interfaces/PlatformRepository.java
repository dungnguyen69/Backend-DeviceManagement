package com.fullstack.Backend.repositories.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fullstack.Backend.entities.Platform;
import com.fullstack.Backend.utils.dropdowns.PlatformList;

public interface PlatformRepository extends JpaRepository<Platform, Long> {
	public static final String FIND_PLATFORM_NAME = "SELECT name FROM Platform";
	public static final String FIND_PLATFORM_VERSION = "SELECT version FROM Platform";
	public static final String FIND_PLATFORM_NAME_VERSION = "SELECT name,version FROM Platform";
	public static final String FIND_PLATFORM = "SELECT p FROM Platform p WHERE name = :name and version = :version";
	public static final String FETCH_PLATFORM = "SELECT p FROM Platform p";

	@Query(FIND_PLATFORM_NAME)
	public List<String> findPlatformName();

	@Query(FIND_PLATFORM_VERSION)
	public List<String> findPlatformVersion();

	@Query(FIND_PLATFORM_NAME_VERSION)
	public List<String> findPlatformNameVersion();

	@Query(FIND_PLATFORM)
	public Platform findByNameAndVersion(String name, String version);

	@Query(FETCH_PLATFORM)
	public List<PlatformList> fetchPlatform();
}
