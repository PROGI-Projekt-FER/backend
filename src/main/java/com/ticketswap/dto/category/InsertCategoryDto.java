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
public class InsertCategoryDto {

    private Long id;
    private String name;
    private String colorHexCode;
    private Long parentCategoryId;

    public static InsertCategoryDto map(Category category) {
        InsertCategoryDto categoryDto = new InsertCategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setColorHexCode(category.getColorHexCode());
        categoryDto.setParentCategoryId(category.getParentCategory().getId());
        return categoryDto;
    }

    public Category toEntity() {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setColorHexCode(colorHexCode);
        if (parentCategoryId != null) {
            category.setParentCategory(new Category(parentCategoryId, null, null, null));
        }
        return category;
    }
}
