package org.habilisoft.zemi.catalog.category.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, CategoryId> {
    @Query("select true from Category c where c.id = :#{#categoryId.value()}")
    boolean exists(CategoryId categoryId);
}
