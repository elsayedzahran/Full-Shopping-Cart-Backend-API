package com.dailyAppTraining.eCommrce.dto;
import com.dailyAppTraining.eCommrce.model.Category;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;


@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private CategoryDto category;
    private List<ImageDto> images;

}
