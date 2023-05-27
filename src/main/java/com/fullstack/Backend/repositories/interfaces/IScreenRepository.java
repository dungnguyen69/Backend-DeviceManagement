package com.fullstack.Backend.repositories.interfaces;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.fullstack.Backend.entities.Screen;
import com.fullstack.Backend.utils.dropdowns.ScreenList;

public interface IScreenRepository extends JpaRepository<Screen, Long> {
	public static final String FIND_SCREEN_SIZES = "select size from screens";
	public static final String FIND_SCREEN = "SELECT * FROM screens WHERE size = ?";
	public static final String FETCH_SCREENS= "SELECT id, size FROM screens";

	@Query(value = FIND_SCREEN_SIZES, nativeQuery = true)
	public List<String> findScreenSize();

	@Query(value = FIND_SCREEN, nativeQuery = true)
	public Screen findBySize(int size);
	
	@Query(value = FETCH_SCREENS, nativeQuery = true)
	public List<ScreenList> fetchScreen();
}
