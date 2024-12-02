package com.dailyAppTraining.eCommrce.service;

import com.dailyAppTraining.eCommrce.dto.AddProductRequest;
import com.dailyAppTraining.eCommrce.dto.UpdateProductRequest;
import com.dailyAppTraining.eCommrce.exception.ResourceNotFoundException;
import com.dailyAppTraining.eCommrce.model.Category;
import com.dailyAppTraining.eCommrce.model.Product;
import com.dailyAppTraining.eCommrce.repository.CategoryRepo;
import com.dailyAppTraining.eCommrce.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;

    public Product addProduct(AddProductRequest request){
        // check if category is found;
        Category category = Optional.ofNullable(categoryRepo.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepo.save(newCategory);
                });
        request.setCategory(category);
        return productRepo.save(createProduct(request, category));
    }
    private Product createProduct(AddProductRequest request, Category category){
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getDescription(),
                request.getPrice(),
                request.getInventory(),
                category
        );
    }
    public Product getProductById(Long id){
        return productRepo.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("product"));
    }
    public List<Product> getAllProducts(){
        return productRepo.findAll();
    }
    public List<Product> getProductsByCategory(String category){
        return productRepo.findByCategoryName(category);
    }
    public List<Product> getProductsByBrand(String brand){
        return productRepo.findByBrand(brand);
    }
    public List<Product> getProductsByCategoryAndBrand(String category, String brand){
        return productRepo.findByCategoryNameAndBrand(category, brand);
    }
    public List<Product> getProductsByName(String name){
        return productRepo.findByName(name);
    }
    public List<Product> getProductsByBrandAndName(String brand, String name){
        return productRepo.findByBrandAndName(brand, name);
    }
    public long countProductsByBrandAndName(String brand, String name){
        return productRepo.countByBrandAndName(brand, name);
    }
    public void deleteProductById(Long id){
        productRepo.findById(id).ifPresentOrElse(productRepo::delete,
                ()-> {throw new ResourceNotFoundException("product");});
    }
    public Product updateProductById(UpdateProductRequest request, long id){
        return productRepo.findById(id)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepo::save)
                .orElseThrow(()-> new ResourceNotFoundException("product"));
    }
    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setPrice(request.getPrice());
        Category category = categoryRepo.findByName(request.getCategory().getName());
        return existingProduct;

    }

}
