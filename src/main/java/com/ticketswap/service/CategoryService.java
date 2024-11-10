package com.ticketswap.service;

import com.ticketswap.dto.category.CategoryDto;
import com.ticketswap.model.Category;
import com.ticketswap.repository.CategoryRepository;
import com.ticketswap.util.ResourceNotFoundException;
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

    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = categoryDto.toEntity();
        category.setId(null);
        category = categoryRepository.save(category);
        return CategoryDto.map(category);
    }

    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category category = categoryDto.toEntity();
        if (category.getId() == null) throw new ResourceNotFoundException("Category Id can not be null");
        if (categoryRepository.findById(categoryDto.getId()).isEmpty()) throw new ResourceNotFoundException("Category with id " + categoryDto.getId() + " not found");
        category = categoryRepository.save(category);
        return CategoryDto.map(category);
    }

    public void deleteCategory(CategoryDto categoryDto) {
        Category category = categoryDto.toEntity();
        if (category.getId() == null) throw new ResourceNotFoundException("Category Id can not be null");
        if (categoryRepository.findById(categoryDto.getId()).isEmpty()) throw new ResourceNotFoundException("Category with id " + categoryDto.getId() + " not found");
        categoryRepository.delete(category);
    }
}
