package com.dailyAppTraining.eCommrce.repository;

import com.dailyAppTraining.eCommrce.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepo extends JpaRepository<Image, Long> {
}