package com.owr.product_service.controller;

import com.owr.product_service.controller.request.ProductWithQuantity;
import com.owr.product_service.dto.ProductDto;
import com.owr.product_service.model.Product;
import com.owr.product_service.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.owr.product_service.exceptions.NoSuchElementException;
import com.owr.product_service.exceptions.InventoryUnavailableException;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    /**
     * Retrieves all products along with their current stock quantities.
     *
     * @return a list of {@link ProductDto} containing product and stock information
     */
    @GetMapping
    public List<ProductDto> getAllProducts() {
        return service.getAllProducts();
    }

    /**
     * Adds a new product to the system and sets its initial stock quantity.
     *
     * @param request a {@code productWithQuantity} object containing product info and quantity
     * @return the newly added product as a {@link ProductDto}
     */
    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@Valid @RequestBody ProductWithQuantity request) {
        ProductDto created = service.addProduct(
                request.product(),
                request.quantity());
        return ResponseEntity.ok(created);
    }

    /**
     * Retrieves a product by its identifier.
     * <p>
     * Returns an HTTP 200 OK with the {@link ProductDto} in the response body if found.
     *
     * @param id the product ID from the path (e.g., {@code /products/42})
     * @return a {@link org.springframework.http.ResponseEntity} wrapping the {@link ProductDto}
     * with status 200 OK
     * @throws NoSuchElementException if no product exists with the given {@code id};
     *                                consider handling this via a {@code @ControllerAdvice} to return 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findProductById(@PathVariable Long id) {
        return ResponseEntity.ok(
                service.findProductById(id)
        );
    }


    /**
     * Retrieves a product by its name.
     * <p>
     * Returns an HTTP 200 OK with the {@link ProductDto} in the response body if found.
     *
     * @param name the product name from the path (e.g., {@code /products/iphone12})
     * @return a {@link org.springframework.http.ResponseEntity} wrapping the {@link ProductDto}
     * with status 200 OK
     * @throws NoSuchElementException if no product exists with the given {@code name};
     *                                consider handling this via a {@code @ControllerAdvice} to return 404 Not Found
     */
    @GetMapping("/{name}")
    public ResponseEntity<ProductDto> findProductByName(@PathVariable String name) {
        return ResponseEntity.ok(
                service.findProductByName(name)
        );
    }

    /**
     * Partially updates a product by its ID.
     * <p>
     * Accepts a JSON payload with one or more fields to change; fields omitted remain unchanged.
     * Returns the updated {@link ProductDto}, enriched with current stock.
     *
     * @param id    the product ID from the path (e.g., {@code /products/42})
     * @param patch a {@link Product} carrying the non-null fields to update
     * @return 200 OK with the updated {@link ProductDto}
     * @throws NoSuchElementException        if no product exists with the given {@code id}
     * @throws InventoryUnavailableException if stock lookup fails during DTO enrichment
     * @throws RuntimeException              if the new name conflicts with an existing product
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id,
                                                    @RequestBody Product patch) {
        ProductDto updated = service.updateProduct(id, patch);
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes a product by its identifier.
     *
     * On success returns HTTP 204 No Content and an empty body.
     *
     * @param id the product ID from the path (e.g., /products/42)
     * @return a 204 No Content response
     * @throws NoSuchElementException if no product exists with the given id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        service.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }


}
