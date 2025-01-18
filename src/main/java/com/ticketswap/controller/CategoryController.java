package com.ticketswap.controller;

import com.ticketswap.dto.category.CategoryDto;
import com.ticketswap.dto.category.InsertCategoryDto;
import com.ticketswap.model.User;
import com.ticketswap.service.AuthService;
import com.ticketswap.service.CategoryService;
import com.ticketswap.util.NotAuthorizedException;
import com.ticketswap.util.NotLoggedInException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private AuthService authService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody InsertCategoryDto categoryDto) {
        authService.getLoggedInAdmin().orElseThrow(NotAuthorizedException::new);

        CategoryDto createdCategory = categoryService.createCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody InsertCategoryDto categoryDto, @PathVariable Long categoryId) {
        authService.getLoggedInAdmin().orElseThrow(NotAuthorizedException::new);
        categoryDto.setId(categoryId);
        CategoryDto updatedCategory = categoryService.updateCategory(categoryDto);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        authService.getLoggedInAdmin().orElseThrow(NotAuthorizedException::new);

        categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
