package com.example.Service;

import java.util.*;
import java.util.stream.Collectors;
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

    // 1. GET ALL (Missing in your previous version)
    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(cat -> modelMapper.map(cat, CategoryResponseDto.class))
                .collect(Collectors.toList());
    }

    // 2. BULK CREATE (Your exact logic)
    public void createCategoriesBulk(List<CategoryRequestDto> dtos) {
        Map<String, Category> tempMap = new HashMap<>();

        // 1️⃣ Save ROOT categories
        for (CategoryRequestDto dto : dtos) {
            if (dto.getParentTempId() == null) {
                Category category = modelMapper.map(dto, Category.class);
                category.setLevel(1);
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

        // 3️⃣ Save nested SUB categories (multi-level do-while)
        boolean pending;
        do {
            pending = false;
            for (CategoryRequestDto dto : dtos) {
                if (dto.getParentTempId() != null && !tempMap.containsKey(dto.getTempId())) {
                    Category parent = tempMap.get(dto.getParentTempId());
                    if (parent != null) {
                        Category category = modelMapper.map(dto, Category.class);
                        category.setParentId(parent.getId());
                        category.setLevel(parent.getLevel() + 1);
                        categoryRepository.save(category);
                        tempMap.put(dto.getTempId(), category);
                    } else {
                        pending = true; 
                    }
                }
            }
        } while (pending);
    }

    // 3. BREADCRUMB (Your exact logic)
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

        Collections.reverse(breadcrumb);
        return breadcrumb;
    }
}