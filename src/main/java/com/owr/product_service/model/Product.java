package com.owr.product_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Product {
    /**
     * The unique identifier for the product.
     * <p>Auto-generated using identity strategy.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the product.
     */
    private String name;

    /**
     * The category of the product.
     */
    private String category;

    /**
     * A brief description of the product.
     */
    private  String description;

    /**
     * The price of the product.
     */
    private Double price;


}
