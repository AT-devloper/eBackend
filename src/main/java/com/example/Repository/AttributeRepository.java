package com.example.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Model.Attribute;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {

}
