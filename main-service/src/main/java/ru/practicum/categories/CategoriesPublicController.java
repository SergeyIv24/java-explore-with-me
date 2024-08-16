package ru.practicum.categories;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.CategoryDto;

import java.util.Collection;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoriesPublicController {

    private final CategoriesService categoriesService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<CategoryDto> getAllCategories(@RequestParam(required = false, value = "from", defaultValue = "0")
                                                    @Min(0) int from,
                                                    @RequestParam(required = false, value = "size", defaultValue = "10")
                                                    @Min(0) int size) {
        log.info("CategoriesPublicController, getAllCategories. From: {}, size: {}", from, size);
        return categoriesService.getAllCategories(from, size);
    }

    @GetMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategoryById(@PathVariable(value = "categoryId") int categoryId) {
        log.info("CategoriesPublicController, getCategoryById. CategoryId: {}", categoryId);
        return categoriesService.getCategoryById(categoryId);
    }

}
