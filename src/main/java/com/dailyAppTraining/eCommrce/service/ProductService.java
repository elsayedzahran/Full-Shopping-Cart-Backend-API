package com.dailyAppTraining.eCommrce.service;

import com.dailyAppTraining.eCommrce.dto.*;
import com.dailyAppTraining.eCommrce.exception.ResourceNotFoundException;
import com.dailyAppTraining.eCommrce.model.Category;
import com.dailyAppTraining.eCommrce.model.Image;
import com.dailyAppTraining.eCommrce.model.ImageRepository;
import com.dailyAppTraining.eCommrce.model.Product;
import com.dailyAppTraining.eCommrce.repository.CategoryRepo;
import com.dailyAppTraining.eCommrce.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;
    private final CategoryService categoryService;

    public ProductDto addProduct(AddProductRequest request){
        // check if category is found;
        Category category = Optional.ofNullable(categoryRepo.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepo.save(newCategory);
                });

        request.setCategory(categoryService.convertToDto(category));
        return convertToDto(productRepo.save(createProduct(request, category)));
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
    public ProductDto getProductDtoById(Long id){
        return convertToDto(productRepo.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("product")));
    }
    public Product getProductById(Long id){
        return productRepo.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("product"));
    }
    public List<ProductDto> getAllProducts(){
        return getConvertedDtoList(productRepo.findAll());
    }
    public List<ProductDto> getProductsByCategory(String category){
        return getConvertedDtoList(productRepo.findByCategoryName(category));
    }
    public List<ProductDto> getProductsByBrand(String brand){
        return getConvertedDtoList(productRepo.findByBrand(brand));
    }
    public List<ProductDto> getProductsByCategoryAndBrand(String category, String brand){
        return getConvertedDtoList(productRepo.findByCategoryNameAndBrand(category, brand));
    }
    public List<ProductDto> getProductsByName(String name){
        return getConvertedDtoList(productRepo.findByName(name));
    }
    public List<ProductDto> getProductsByBrandAndName(String brand, String name){
        return getConvertedDtoList(productRepo.findByBrandAndName(brand, name));
    }
    public long countProductsByBrandAndName(String brand, String name){
        return productRepo.countByBrandAndName(brand, name);
    }
    public void deleteProductById(Long id){
        productRepo.findById(id).ifPresentOrElse(productRepo::delete,
                ()-> {throw new ResourceNotFoundException("product");});
    }
    public ProductDto updateProductById(UpdateProductRequest request, long id){
        return convertToDto(productRepo.findById(id)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepo::save)
                .orElseThrow(()-> new ResourceNotFoundException("product")));
    }
    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setPrice(request.getPrice());
        Category category = categoryRepo.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;

    }

    public List<ProductDto> getConvertedDtoList(List<Product> products){
        return products.stream().map(this::convertToDto).toList();
    }

    public ProductDto convertToDto(Product product){
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDto = images.stream()
                .map((image) -> modelMapper.map(image, ImageDto.class)).toList();
        productDto.setImages(imageDto);
        return productDto;
    }
}
