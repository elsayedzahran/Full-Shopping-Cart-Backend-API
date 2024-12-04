package com.dailyAppTraining.eCommrce.service;

import com.dailyAppTraining.eCommrce.dto.CategoryDto;
import com.dailyAppTraining.eCommrce.dto.ImageDto;
import com.dailyAppTraining.eCommrce.dto.ProductDto;
import com.dailyAppTraining.eCommrce.exception.AlreadyExistException;
import com.dailyAppTraining.eCommrce.exception.ResourceNotFoundException;
import com.dailyAppTraining.eCommrce.model.Category;
import com.dailyAppTraining.eCommrce.model.Image;
import com.dailyAppTraining.eCommrce.model.Product;
import com.dailyAppTraining.eCommrce.repository.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final ModelMapper modelMapper;
    private final CategoryRepo categoryRepo;

    public List<Category> getAllCategories(){
        return categoryRepo.findAll();
    }
    public Category getCategoryById(Long id){
        return categoryRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("category"));
    }
    public Category getCategoryByName(String name){
        return categoryRepo.findByName(name);
    }
    public Category addCategory(Category category){
        return Optional.of(category)
                .filter(c -> !categoryRepo.existsByName(c.getName()))
                .map(categoryRepo::save)
                .orElseThrow(()-> new AlreadyExistException("category"));
               // .ifPresentOrElse(categoryRepo::save, CategoryNotFoundException::new); do same work as above 2 lines
    }
    public Category updateCategory(Category category, Long id){
        return Optional.ofNullable(getCategoryById(id)).map(oldCategory -> {
            oldCategory.setName(category.getName());
            return categoryRepo.save(oldCategory);
        }).orElseThrow(() -> new ResourceNotFoundException("category"));
    }
    public void deleteCategoryById(Long id){
        categoryRepo.findById(id).ifPresentOrElse(
                categoryRepo::delete, () -> {throw new ResourceNotFoundException("category");});
    }

    public CategoryDto convertToDto(Category category){
        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
        return categoryDto;
    }
}
