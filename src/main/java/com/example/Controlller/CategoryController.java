package com.example.Controlller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Dto.CategoryRequestDto;
import com.example.Dto.CategoryResponseDto;
import com.example.Service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	@Autowired
	CategoryService categoryService;
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

