package com.owr.product_service.repository;

import com.owr.product_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * Finds a product by its name.
     *
     * @param name the name of the product to search for
     * @return an {@link Optional} containing the product if found, or empty if not
     */
    Optional<Product> findByName(String name);
}
