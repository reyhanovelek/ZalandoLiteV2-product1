package com.owr.product_service.service;

import com.owr.product_service.dto.ProductDto;
import com.owr.product_service.exceptions.InventoryUnavailableException;
import com.owr.product_service.exceptions.NoSuchElementException;
import com.owr.product_service.mapper.ProductMapper;
import com.owr.product_service.model.Product;
import com.owr.product_service.repository.ProductRepository;
import com.owr.product_service.service.client.InventoryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final InventoryClient client;

    /**
     * Retrieves all products from the database and maps them to DTOs enriched with
     * real-time stock quantity from the inventory service.
     *
     * @return a list of {@link ProductDto} containing product details and current stock levels
     */
    public List<ProductDto> getAllProducts() {
        return repository.findAll().stream()
                .map(p-> ProductMapper.toDTO(
                        p, safeGetStock(p.getId())
                )).toList();
    }

    /**
     * Adds a new product to the system and initializes its stock in the inventory service.
     *
     * <p>This method first checks if a product with the same name already exists. If it does,
     * a {@link RuntimeException} is thrown. Otherwise, the product is saved in the database and
     * the stock quantity is initialized in the inventory service.</p>
     *
     * @param product  the product entity to be added
     * @param quantity the initial stock quantity to be registered in the inventory
     * @return the saved product as a {@link ProductDto}, including stock quantity
     * @throws RuntimeException if a product with the same name already exists
     */
    @Transactional
    public ProductDto addProduct(Product product, int quantity) {
        Optional<Product> existing = repository.findByName(product.getName());
        if (existing.isPresent()) {
            throw new RuntimeException("Product already exists");
        }

        Product savedProduct = repository.save(product);
        client.createInventory(savedProduct.getId(), quantity);

        return ProductMapper.toDTO(savedProduct, quantity);
    }

    /**
     * Finds a product by its id and enriches it with the current stock from the inventory service.
     *
     * <p>Delegates to {@link #safeGetStock(Long)} for the stock lookup.</p>
     *
     * @param id the product identifier
     * @return a {@link ProductDto} containing the product details and current stock
     * @throws NoSuchElementException        if no product exists with the given id
     * @throws InventoryUnavailableException if the inventory lookup fails
     */
    public ProductDto findProductById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + id));



        return ProductMapper.toDTO(product, safeGetStock(id));

    }

    /**
     * Finds a product by its name and enriches it with the current stock from the inventory service.
     *
     * <p>Delegates to {@link #safeGetStock(Long)} for the stock lookup.</p>
     *
     * @param name the unique product name
     * @return a {@link ProductDto} containing the product details and current stock
     * @throws NoSuchElementException        if no product exists with the given id
     * @throws InventoryUnavailableException if the inventory lookup fails
     */
    public ProductDto findProductByName(String name) {
        Product product = repository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + name));


        return ProductMapper.toDTO(product, safeGetStock(product.getId()));

    }


    /**
     * Updates an existing {@link Product} by applying a partial patch of its non-null fields and returns a
     * {@link ProductDto} enriched with the current stock quantity from the inventory service.
     * <p>
     * Behavior
     * - Loads the existing product by {@code id}; throws if not found.
     * - If {@code patch.name} is provided and different, ensures name uniqueness before updating.
     * - Applies other non-null fields from {@code patch} (e.g., category, description, price)
     * - Saves the changes and fetches current stock via {@link #safeGetStock(Long)}.
     *
     * @param id    the identifier of the product to update
     * @param patch a {@link Product} whose non-null fields represent updates to apply
     * @return the updated product mapped to {@link ProductDto}, including current stock
     * @throws NoSuchElementException        if no product exists with the given {@code id}
     * @throws RuntimeException              if the provided name already exists for a different product
     * @throws InventoryUnavailableException if stock lookup fails in {@link #safeGetStock(Long)}
     */
    public ProductDto updateProduct(Long id, Product patch) {
        Product existing = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + id));


        // If name is provided and changed, ensure uniqueness
        if (patch.getName() != null
                && !patch.getName().equals(existing.getName())) {
            repository.findByName(patch.getName()).ifPresent(p -> {
                throw new RuntimeException("Product name already exists: " + patch.getName());
            });
            existing.setName(patch.getName());
        }

        // Patch other fields if provided as needed)
        if (patch.getCategory() != null) {
            existing.setCategory(patch.getCategory());
        }

        if (patch.getDescription() != null) {
            existing.setDescription(patch.getDescription());
        }

        if (patch.getPrice() != null) {
            existing.setPrice(patch.getPrice());
        }
        // ... add any other fields


        Product savedProduct = repository.save(existing);

        // no inventory update here — inventory service handles quantity itself
        int qty = safeGetStock(id);
        return ProductMapper.toDTO(savedProduct, qty);
    }

    /**
     * Deletes a product by its identifier.
     * <p>No inventory updates are performed here; the inventory service manages quantity separately.</p>
     *
     * @param id the product id to delete
     * @throws NoSuchElementException if no product exists with the given id
     */
    public void deleteProductById(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Product not found: " + id);
        }
        repository.deleteById(id);
    }

    public Double getUnitPrice(Long productId) {

        return repository.findById(productId)
                .map(Product::getPrice)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + productId));
    }
    /**========================================================================
     * Helper Methods
     ===========================================================================*/
    /**
     * Retrieves the current available stock for the given product by calling the inventory service.
     *
     * @param productId the product identifier
     * @return the available stock quantity
     * @throws InventoryUnavailableException if the inventory service call fails for any reason
     */
    private int safeGetStock(Long productId) {
        try {
            return client.getStockQuantity(productId);
        } catch (RuntimeException e) {
            throw new InventoryUnavailableException(
                    "Inventory lookup failed for product " + productId, e
            );
        }
    }

}
