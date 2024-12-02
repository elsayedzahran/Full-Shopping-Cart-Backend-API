package com.dailyAppTraining.eCommrce.dto;

import com.dailyAppTraining.eCommrce.model.Category;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddProductRequest {
    private String name;
    private String description;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private Category category;
}
