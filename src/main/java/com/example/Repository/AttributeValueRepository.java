package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Model.AttributeValue;
import java.util.List;


public interface AttributeValueRepository extends JpaRepository<AttributeValue, Long> {
	
		List<AttributeValue> findByAttributeId(Long attributeId);
		boolean existsByIdAndAttributeId(Long id, Long attributeId);

}
