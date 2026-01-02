package com.example.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Dto.BrandRequestDto;
import com.example.Dto.BrandResponseDto;
import com.example.Model.Brand;
import com.example.Repository.BrandRepository;

@Service
public class BrandService {
    
	@Autowired
	BrandRepository brandRepository;
	@Autowired
    ModelMapper modelMapper;

    // BULK CREATE
    public void createBrandsBulk(List<BrandRequestDto> dtos) {
        List<Brand> brands = dtos.stream()
                .map(dto -> modelMapper.map(dto, Brand.class))
                .collect(Collectors.toList());
        brandRepository.saveAll(brands);
    }

    // GET ALL ACTIVE BRANDS
    public List<BrandResponseDto> getAllBrands() {
        return brandRepository.findAll()
                .stream()
                .map(brand -> modelMapper.map(brand, BrandResponseDto.class))
                .collect(Collectors.toList());
    }
}
