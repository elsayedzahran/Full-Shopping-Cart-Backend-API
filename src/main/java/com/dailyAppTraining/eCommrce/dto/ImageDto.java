package com.dailyAppTraining.eCommrce.dto;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
public class ImageDto {
    private Long id;
    private String fileName;
    private String downloadUrl;

}
