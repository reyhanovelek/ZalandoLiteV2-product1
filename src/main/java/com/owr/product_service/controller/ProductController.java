package com.owr.product_service.controller;

import com.owr.product_service.model.Product;
import com.owr.product_service.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }
    // LIST
    @GetMapping
    public List<Product> all() {
        return service.findAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public Product one(@PathVariable Long id) {
        return service.findById(id);
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product p) {
        Product saved = service.create(p);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product p) {
        return service.update(id, p);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
