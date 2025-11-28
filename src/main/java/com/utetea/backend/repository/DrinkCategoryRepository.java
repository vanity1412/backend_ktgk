package com.utetea.backend.repository;
//VU VAN THONG 23162098
import com.utetea.backend.model.DrinkCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrinkCategoryRepository extends JpaRepository<DrinkCategory, Long> {
    
    List<DrinkCategory> findByIsActiveTrueOrderByDisplayOrderAsc();
    
    List<DrinkCategory> findAllByOrderByDisplayOrderAsc();
}
