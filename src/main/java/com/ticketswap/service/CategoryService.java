package com.ticketswap.service;

import com.ticketswap.dto.category.CategoryDto;
import com.ticketswap.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryDto> getAllCategories() {
        List<CategoryDto> categories = categoryRepository.findAll().stream().map(CategoryDto::map).toList();
        return categories;
    }
}
