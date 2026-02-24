package com.example.Controlller; 

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.Dto.ProductImageResponseDto;
import com.example.Service.ProductImageService;

@RestController
@RequestMapping("/api/products/{productId}/images")
public class ProductImageController {

    @Autowired
    private ProductImageService service;

    // BULK UPLOAD IMAGES
    @PostMapping
    public String uploadImages(
            @PathVariable Long productId,
            @RequestParam(required = false) Long variantId,
            @RequestParam("files") List<MultipartFile> files
    ) throws Exception {
        service.uploadImages(productId, variantId, files);
        return "Images uploaded successfully";
    }

    // SET PRIMARY IMAGE
    @PutMapping("/{imageId}/set-primary")
    public String setPrimary(@PathVariable Long imageId) {
        service.setPrimaryImage(imageId);
        return "Primary image set";
    }

 // GET IMAGES
    @GetMapping
    public List<ProductImageResponseDto> getImages(
            @PathVariable Long productId,
            @RequestParam(required = false) Long variantId
    ) {
        return service.getImages(productId, variantId);
    }

}
