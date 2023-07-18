package com.fullstack.Backend.repositories.interfaces;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.fullstack.Backend.entities.Screen;
import com.fullstack.Backend.utils.dropdowns.ScreenList;
import org.springframework.stereotype.Repository;

@Repository

public interface ScreenRepository extends JpaRepository<Screen, Long> {
	public static final String FIND_SCREEN_SIZES = "SELECT size FROM Screen";
	public static final String FIND_SCREEN = "SELECT s FROM Screen s WHERE size = :size";
	public static final String FETCH_SCREENS= "SELECT s FROM Screen s";

	@Query(FIND_SCREEN_SIZES)
	public List<String> findScreenSize();

	@Query(FIND_SCREEN)
	public Screen findBySize(String size);
	
	@Query(FETCH_SCREENS)
	public List<ScreenList> fetchScreen();
}
