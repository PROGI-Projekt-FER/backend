package com.ticketswap.dto.category;

import com.ticketswap.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private Long id;
    private String name;
    private Long parentCategoryId;
    private String parentCategoryName;

    public static CategoryDto map(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        if (category.getParentCategory() != null) {
            categoryDto.setParentCategoryId(category.getParentCategory().getId());
            categoryDto.setParentCategoryName(category.getParentCategory().getName());
        }
        return categoryDto;
    }

    public Category toEntity() {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setParentCategory(new Category(parentCategoryId, parentCategoryName, null));
        return category;
    }
}
