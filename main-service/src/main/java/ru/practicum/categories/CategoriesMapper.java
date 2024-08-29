package ru.practicum.categories;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.model.Category;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoriesMapper {

    public static Category mapToCategory(CategoryDto categoryDto) {
        return new Category(categoryDto.getId(), categoryDto.getName());
    }

    public static CategoryDto mapToCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
