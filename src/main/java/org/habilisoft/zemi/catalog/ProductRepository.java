package org.habilisoft.zemi.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
interface ProductRepository extends JpaRepository<Product, ProductId> {}
