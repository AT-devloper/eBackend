package com.example.Controlller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.Dto.CategoryRequestDto;
import com.example.Dto.CategoryResponseDto;
import com.example.Service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Fetch all categories for your frontend dropdowns
    @GetMapping
    public List<CategoryResponseDto> getAll() {
        return categoryService.getAllCategories();
    }

    @PostMapping("/bulk") 
    public String createCategories(@RequestBody List<CategoryRequestDto> dtos) { 
        categoryService.createCategoriesBulk(dtos); 
        return "Categories created successfully"; 
    } 

    @GetMapping("/breadcrumb/{categoryId}") 
    public List<CategoryResponseDto> getBreadcrumb(@PathVariable Long categoryId) { 
        return categoryService.getBreadcrumb(categoryId); 
    } 
}