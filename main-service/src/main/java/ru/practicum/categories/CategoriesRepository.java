package ru.practicum.categories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.categories.model.Category;

@Repository
public interface CategoriesRepository extends JpaRepository<Category, Integer> {
}
