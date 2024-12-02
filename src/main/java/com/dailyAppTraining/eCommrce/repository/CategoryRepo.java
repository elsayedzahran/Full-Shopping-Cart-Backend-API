package com.dailyAppTraining.eCommrce.repository;

import com.dailyAppTraining.eCommrce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    Category findByName(String name);

    boolean existsByName(String name);
}
