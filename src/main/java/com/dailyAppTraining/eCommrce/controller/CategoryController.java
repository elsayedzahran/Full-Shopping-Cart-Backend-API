package com.dailyAppTraining.eCommrce.controller;


import com.dailyAppTraining.eCommrce.exception.AlreadyExistException;
import com.dailyAppTraining.eCommrce.exception.ResourceNotFoundException;
import com.dailyAppTraining.eCommrce.model.Category;
import com.dailyAppTraining.eCommrce.response.ApiResponse;
import com.dailyAppTraining.eCommrce.service.CategoryService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories(){
        try {
            return ResponseEntity.ok(new ApiResponse("Found: ",categoryService.getAllCategories()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                                 .body(new ApiResponse("Error!!", INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category){
        try {
            return ResponseEntity.ok(new ApiResponse("Added: ",categoryService.addCategory(category)));
        } catch (AlreadyExistException e) {
            return ResponseEntity.status(CONFLICT)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
    @GetMapping("/category/{categoryId}/get")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long categoryId){
        try {
            return ResponseEntity.ok(new ApiResponse("Found: ",categoryService.getCategoryById(categoryId)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{categoryName}/get")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String categoryName){
        try {
            return ResponseEntity.ok(new ApiResponse("Found: ",categoryService.getCategoryByName(categoryName)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("category/{categoryId}/delete")
    public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable Long categoryId){
        try {
            categoryService.deleteCategoryById(categoryId);
            return ResponseEntity.ok(new ApiResponse("Deleted!! ",null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("category/{categoryId}/update")
    public ResponseEntity<ApiResponse> updateCategoryById(@PathVariable Long categoryId, @RequestBody Category category){
        try {
            return ResponseEntity.ok(new ApiResponse("updated!! ",categoryService.updateCategory(category, categoryId)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

}
