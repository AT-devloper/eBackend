package com.example.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.Dto.CategoryRequestDto;
import com.example.Dto.CategoryResponseDto;
import com.example.Model.Category;
import com.example.Repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    
      //Create categories in bulk (root → child → subcategories)
     
    public void createCategoriesBulk(List<CategoryRequestDto> dtos) {
        Map<String, Category> tempMap = new HashMap<>();

        // 1️⃣ Save ROOT categories
        for (CategoryRequestDto dto : dtos) {
            if (dto.getParentTempId() == null) {
                Category category = modelMapper.map(dto, Category.class);
                category.setLevel(1); // root level
                categoryRepository.save(category);
                tempMap.put(dto.getTempId(), category);
            }
        }

        // 2️⃣ Save CHILD categories (first pass)
        for (CategoryRequestDto dto : dtos) {
            if (dto.getParentTempId() != null) {
                Category parent = tempMap.get(dto.getParentTempId());
                if (parent != null) {
                    Category category = modelMapper.map(dto, Category.class);
                    category.setParentId(parent.getId());
                    category.setLevel(parent.getLevel() + 1);
                    categoryRepository.save(category);
                    tempMap.put(dto.getTempId(), category);
                }
            }
        }

        // 3️⃣ Save nested SUB categories (multi-level)
        boolean pending;
        do {
            pending = false;

            for (CategoryRequestDto dto : dtos) {
                if (dto.getParentTempId() != null
                        && !tempMap.containsKey(dto.getTempId())) {
                    Category parent = tempMap.get(dto.getParentTempId());
                    if (parent != null) {
                        Category category = modelMapper.map(dto, Category.class);
                        category.setParentId(parent.getId());
                        category.setLevel(parent.getLevel() + 1);
                        categoryRepository.save(category);
                        tempMap.put(dto.getTempId(), category);
                    } else {
                        pending = true; // parent not yet created
                    }
                }
            }
        } while (pending);
    }

    /**
     * Get breadcrumb (root → current category)
     */
    public List<CategoryResponseDto> getBreadcrumb(Long categoryId) {
        List<CategoryResponseDto> breadcrumb = new ArrayList<>();
        Category current = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        while (current != null) {
            breadcrumb.add(modelMapper.map(current, CategoryResponseDto.class));
            current = current.getParentId() == null
                    ? null
                    : categoryRepository.findById(current.getParentId()).orElse(null);
        }

        Collections.reverse(breadcrumb); // root → current
        return breadcrumb;
    }
}
