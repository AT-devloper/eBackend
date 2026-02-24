package com.example.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Dto.AttributeRequestDto;
import com.example.Dto.AttributeResponseDto;
import com.example.Model.Attribute;
import com.example.Model.AttributeValue;
import com.example.Repository.AttributeRepository;
import com.example.Repository.AttributeValueRepository;

@Service
public class AttributeService {

    @Autowired
    private AttributeRepository attributeRepo;

    @Autowired
    private AttributeValueRepository valueRepo;

    // CREATE ATTRIBUTE + VALUES (BULK)
    public void createAttribute(AttributeRequestDto dto) {
        Attribute attribute = new Attribute();
        attribute.setName(dto.getName());
        attributeRepo.save(attribute);

        for (String val : dto.getValues()) {
            AttributeValue value = new AttributeValue();
            value.setAttributeId(attribute.getId());
            value.setValue(val);
            valueRepo.save(value);
        }
    }

    // GET ATTRIBUTE WITH VALUES
    public AttributeResponseDto getAttribute(Long attributeId) {
        Attribute attribute = attributeRepo.findById(attributeId)
                .orElseThrow(() -> new RuntimeException("Attribute not found"));

        List<String> values = valueRepo.findByAttributeId(attributeId)
                .stream()
                .map(AttributeValue::getValue)
                .toList();

        AttributeResponseDto res = new AttributeResponseDto();
        res.setId(attribute.getId());
        res.setName(attribute.getName());
        res.setValues(values);

        return res;
    }

    // GET ALL ATTRIBUTES WITH VALUES
    public List<AttributeResponseDto> getAllAttributes() {
        return attributeRepo.findAll().stream().map(attribute -> {

            List<String> values = valueRepo.findByAttributeId(attribute.getId())
                    .stream()
                    .map(AttributeValue::getValue)
                     .toList();

            AttributeResponseDto dto = new AttributeResponseDto();
            dto.setId(attribute.getId());
            dto.setName(attribute.getName());
            dto.setValues(values);

            return dto;
        }).toList();
    }
}
