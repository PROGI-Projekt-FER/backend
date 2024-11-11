package com.ticketswap.service;

import com.ticketswap.dto.category.CategoryDto;
import com.ticketswap.dto.category.InsertCategoryDto;
import com.ticketswap.model.Category;
import com.ticketswap.repository.CategoryRepository;
import com.ticketswap.util.ResourceNotFoundException;
import jakarta.transaction.Transactional;
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

    public CategoryDto createCategory(InsertCategoryDto categoryDto) {
        Category category = categoryDto.toEntity();
        category.setId(null);
        if (category.getParentCategory() != null) {
            category.setParentCategory(categoryRepository.findById(category.getParentCategory().getId()).get());
        }
        category = categoryRepository.save(category);
        return CategoryDto.map(category);
    }

    public CategoryDto updateCategory(InsertCategoryDto categoryDto) {
        Category category = categoryDto.toEntity();
        if (category.getId() == null) throw new ResourceNotFoundException("Category Id can not be null");
        if (categoryRepository.findById(categoryDto.getId()).isEmpty()) throw new ResourceNotFoundException("Category with id " + categoryDto.getId() + " not found");
        category = categoryRepository.save(category);
        return CategoryDto.map(category);
    }

    public void deleteCategory(Long categoryId) {
        if (categoryId == null) throw new ResourceNotFoundException("Category Id can not be null");
        Category category = new Category();
        category.setId(categoryId);
        if (categoryRepository.findById(categoryId).isEmpty()) throw new ResourceNotFoundException("Category with id " + categoryId + " not found");
        categoryRepository.delete(category);
    }
}
