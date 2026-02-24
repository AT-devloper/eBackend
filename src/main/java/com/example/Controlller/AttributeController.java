package com.example.Controlller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.Dto.AttributeRequestDto;
import com.example.Dto.AttributeResponseDto;
import com.example.Service.AttributeService;

@RestController
@RequestMapping("/api/attributes")
public class AttributeController {

    @Autowired
    AttributeService service;

    @PostMapping
    public String createAttribute(@RequestBody AttributeRequestDto dto) {
        service.createAttribute(dto);
        return "Attribute created";
    }

    @GetMapping
    public List<AttributeResponseDto> getAllAttributes() {
        return service.getAllAttributes();
    }

    @GetMapping("/{id}")
    public AttributeResponseDto getAttribute(@PathVariable Long id) {
        return service.getAttribute(id);
    }
}
