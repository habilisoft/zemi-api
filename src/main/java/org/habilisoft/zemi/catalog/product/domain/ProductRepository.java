package org.habilisoft.zemi.catalog.product.domain;

import org.springframework.data.jpa.repository.JpaRepository;
public interface ProductRepository extends JpaRepository<Product, ProductId> {}
