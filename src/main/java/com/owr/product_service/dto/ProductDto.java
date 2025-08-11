package com.owr.product_service.dto;


import lombok.*;

/*=================================================================================
 * Project: product-service
 * File: ProductDto
 * Created by: Ochwada
 * Created on: 11, 8/11/2025, 11:00 AM
 * Description:   DTO for exposing product to clients, including stock quantity
 =================================================================================*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    /**
     * The unique identifier for the product.
     * <p>Auto-generated using identity strategy.</p>
     */
    private Long id;

    /**
     * The name of the product.
     */
    private String name;

    /**
     * The price of the product.
     */
    private double price;

    /**
     * The category of the product.
     */
    private String category;

    /**
     * A brief description of the product.
     */
    private String description;

    /**
     * The quantity of the product in stock.
     */
    private int quantity;
}
