package com.owr.product_service.mapper;


import com.owr.product_service.dto.ProductDto;
import com.owr.product_service.model.Product;

/*=================================================================================
 * Project: product-service
 * File: ProductMapper
 * Created by: Ochwada
 * Created on: 11, 8/11/2025, 11:03 AM
 * Description:  Utility class for converting between {@link Product} entity and {@link ProductDto}.
 =================================================================================*/
public class ProductMapper {
    /**
     * Converts a {@link Product} entity to a {@link ProductDto}, attaching the specified quantity.
     *
     * @param product  the product entity to convert
     * @param quantity the quantity of the product in stock to include in the DTO
     * @return a new {@code ProductDto} containing product details and stock quantity
     */

    public static ProductDto toDTO (Product product, int quantity){
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getCategory(),
                quantity
        );
    }
}
