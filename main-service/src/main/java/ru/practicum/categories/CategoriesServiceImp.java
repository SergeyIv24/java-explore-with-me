package ru.practicum.categories;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.errors.NotFoundException;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.model.Category;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoriesServiceImp implements CategoriesService {

    private final CategoriesRepository categoriesRepository;

    @Override
    public CategoryDto addCategory(@Valid CategoryDto categoryDto) {
        return CategoriesMapper
                .mapToCategoryDto(categoriesRepository.save(CategoriesMapper.mapToCategory(categoryDto)));
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, int categoryId) {
        Category updatingCategory = validateCategory(categoryId);
        updatingCategory.setName(categoryDto.getName());
        return CategoriesMapper.mapToCategoryDto(categoriesRepository.save(updatingCategory));
    }

    @Override
    public void deleteCategory(int categoryId) {
        validateCategory(categoryId);
        categoriesRepository.deleteById(categoryId);
    }

    @Override
    public Collection<CategoryDto> getAllCategories(int from, int size) {
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        int startPage = from > 0 ? (from / size) : 0;
        Pageable pageable = PageRequest.of(startPage, size, sortById);
        return categoriesRepository.findAll(pageable)
                .stream()
                .map(CategoriesMapper::mapToCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(int categoryId) {
        Category category = validateCategory(categoryId);
        return CategoriesMapper.mapToCategoryDto(category);
    }

    private Category validateCategory(int categoryId) {
        Optional<Category> category = categoriesRepository.findById(categoryId);

        if (category.isEmpty()) {
            log.warn("Attempt to delete unknown category with categoryId: {}", categoryId);
            throw new NotFoundException("Category with id = " + categoryId + " was not found");
        }
        return category.get();
    }
}
