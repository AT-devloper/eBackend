package com.example.Repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>  {
	List<Category> findByParentId(Long parentId);
	List<Category> findByParentIdIsNull();
}
