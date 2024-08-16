package ru.practicum.categories;

import ru.practicum.categories.dto.CategoryDto;

import java.util.Collection;

public interface CategoriesService {

    CategoryDto addCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto, int categoryId);

    void deleteCategory(int categoryId);

    Collection<CategoryDto> getAllCategories(int from, int size);

    CategoryDto getCategoryById(int categoryId);

}
