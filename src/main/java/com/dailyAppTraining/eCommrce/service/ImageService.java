package com.dailyAppTraining.eCommrce.service;

import com.dailyAppTraining.eCommrce.dto.ImageDto;
import com.dailyAppTraining.eCommrce.dto.ProductDto;
import com.dailyAppTraining.eCommrce.exception.ResourceNotFoundException;
import com.dailyAppTraining.eCommrce.model.Image;
import com.dailyAppTraining.eCommrce.model.Product;
import com.dailyAppTraining.eCommrce.repository.ImageRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ImageService {
    private final ImageRepo imageRepo;
    private final ProductService productService;


    public Image getImageById(Long id){
        return imageRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("image"));
    }
    public List<ImageDto> saveImage(List<MultipartFile> files, Long productId){
        Product product = productService.getProductById(productId);
        List<ImageDto> savedImagesDtos = new ArrayList<>();
        for (MultipartFile file: files){
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String downloadUrl = "/api/v1/images/image/download/" + image.getId();
                image.setDownloadUrl(downloadUrl);

                Image savedImage = imageRepo.save(image);
                savedImage.setDownloadUrl("/api/v1/images/image/download/" + savedImage.getId());
                imageRepo.save(savedImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setFileName(savedImage.getFileName());
                imageDto.setId(savedImage.getId());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());

                savedImagesDtos.add(imageDto);
            }catch (IOException | SQLException e){
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImagesDtos;
    }
    public void updateImage(MultipartFile file, Long imageId){
        Image image = imageRepo.findById(imageId).orElseThrow(() -> new ResourceNotFoundException("image", imageId));
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepo.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
    public void deleteImageById(Long id){
        imageRepo.findById(id).ifPresentOrElse(
                imageRepo::delete, ()-> {throw new ResourceNotFoundException("image", id);});
    }
}
