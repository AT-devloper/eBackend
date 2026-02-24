package com.example.Service;

import java.util.*;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.example.Dto.ProductImageResponseDto;
import com.example.Model.ProductImage;
import com.example.Repository.ProductImageRepository;

@Service
public class ProductImageService {

    @Autowired
    private ProductImageRepository repository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private ModelMapper modelMapper;

    // ----------------------------
    // BULK UPLOAD IMAGES
    // ----------------------------
    public void uploadImages(Long productId, Long variantId, List<MultipartFile> files) throws Exception {
        int order = 1;

        for (MultipartFile file : files) {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), Map.of());

            ProductImage image = new ProductImage();
            image.setProductId(productId);
            image.setVariantId(variantId);
            image.setImageUrl(uploadResult.get("secure_url").toString());
            
            // FIX: Set primary BEFORE incrementing the order
            image.setIsPrimary(order == 1); 
            image.setDisplayOrder(order++);

            repository.save(image);
        }
    }

    // ----------------------------
    // SET PRIMARY IMAGE
    // ----------------------------
    public void setPrimaryImage(Long imageId) {
        ProductImage image = repository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        List<ProductImage> images = (image.getVariantId() == null)
                ? repository.findByProductIdAndVariantIdIsNull(image.getProductId())
                : repository.findByProductIdAndVariantId(image.getProductId(), image.getVariantId());

        images.forEach(img -> img.setIsPrimary(false));
        repository.saveAll(images);

        image.setIsPrimary(true);
        repository.save(image);
    }
    
    // ----------------------------
    // GET PRIMARY IMAGE FOR SINGLE PRODUCT
    // ----------------------------
    public String getPrimaryImageUrl(Long productId) {
        // Updated to find the first primary image available
        List<ProductImage> images = repository.findByProductIdAndIsPrimaryTrue(productId);
        if (images.isEmpty()) {
            return "/placeholder.png"; 
        }
        return images.get(0).getImageUrl();
    }

    // ----------------------------
    // GET IMAGES FOR A SINGLE PRODUCT
    // ----------------------------
    public List<ProductImageResponseDto> getImages(Long productId, Long variantId) {
        // LOGIC: Get Variant Images
        List<ProductImage> vImages = (variantId == null) 
            ? new ArrayList<>() 
            : repository.findByProductIdAndVariantId(productId, variantId);
            
        // LOGIC: Get General Images
        List<ProductImage> gImages = repository.findByProductIdAndVariantIdIsNull(productId);

        // Combine them so the gallery is never empty
        List<ProductImage> combined = new ArrayList<>();
        combined.addAll(vImages);
        combined.addAll(gImages);

        if (combined.isEmpty()) {
            ProductImageResponseDto placeholder = new ProductImageResponseDto();
            placeholder.setProductId(productId);
            placeholder.setImageUrl("/placeholder.png");
            return List.of(placeholder);
        }

        return combined.stream()
                .map(img -> modelMapper.map(img, ProductImageResponseDto.class))
                .collect(Collectors.toList());
    }

    // ----------------------------
    // GET PRIMARY IMAGES FOR MULTIPLE PRODUCTS
    // ----------------------------
    public List<ProductImageResponseDto> getPrimaryImages(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) return Collections.emptyList();

        List<ProductImage> images = repository.findAllByProductIdInAndIsPrimaryTrue(productIds);

        Map<Long, ProductImageResponseDto> imageMap = images.stream()
            .collect(Collectors.toMap(
                ProductImage::getProductId,
                img -> modelMapper.map(img, ProductImageResponseDto.class),
                (existing, replacement) -> existing // Keep the first primary found
            ));

        for (Long pid : productIds) {
            if (!imageMap.containsKey(pid)) {
                ProductImageResponseDto placeholder = new ProductImageResponseDto(
                    null, pid, null, "/placeholder.png", false, 0
                );
                imageMap.put(pid, placeholder);
            }
        }

        return new ArrayList<>(imageMap.values());
    }
}