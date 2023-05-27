package com.fullstack.Backend.repositories.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fullstack.Backend.entities.Platform;
import com.fullstack.Backend.utils.dropdowns.PlatformList;

public interface IPlatformRepository extends JpaRepository<Platform, Long> {
	public static final String FIND_PLATFORM_NAME = "select name from platform";
	public static final String FIND_PLATFORM_VERSION = "select version from platform";
	public static final String FIND_PLATFORM_NAME_VERSION = "select name,version from platform";
	public static final String FIND_PLATFORM = "SELECT * FROM platform WHERE name = ?1 and version = ?2";
	public static final String FETCH_PLATFORM = "SELECT id, name, version FROM platform";

	@Query(value = FIND_PLATFORM_NAME, nativeQuery = true)
	public List<String> findPlatformName();

	@Query(value = FIND_PLATFORM_VERSION, nativeQuery = true)
	public List<String> findPlatformVersion();

	@Query(value = FIND_PLATFORM_NAME_VERSION, nativeQuery = true)
	public List<String> findPlatformNameVersion();

	@Query(value = FIND_PLATFORM, nativeQuery = true)
	public Platform findByNameAndVersion(String name, String version);

	@Query(value = FETCH_PLATFORM, nativeQuery = true)
	public List<PlatformList> fetchPlatform();
}
