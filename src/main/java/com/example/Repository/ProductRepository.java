package com.example.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
	
	Optional<Product>findBySlug(String slug);
}
