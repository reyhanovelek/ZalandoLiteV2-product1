package com.owr.product_service.controller.request;


import com.owr.product_service.model.Product;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/*=================================================================================
 * Project: product-service
 * File: ProductWithQuantity
 * Created by: Ochwada
 * Created on: 11, 8/11/2025, 12:00 PM
 * Description:  Request payload for creating a product along with its stock quantity.
 =================================================================================*/
/**
 * Request payload for creating a product along with its stock quantity.
 *
 * @param product  the product to be created
 * @param quantity the initial quantity to set in the inventory
 */
public record ProductWithQuantity(
        @Valid @NotNull Product product,
        @NotNull @PositiveOrZero int quantity) {
}
