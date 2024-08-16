package ru.practicum.categories;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.model.Category;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoriesMapper {

    public static Category mapToCategory(CategoryDto categoryDto) {
        return Category
                .builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }

    public static CategoryDto mapToCategoryDto(Category category) {
        return CategoryDto
                .builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

}
