package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Model.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {

}
