package com.owr.product_service.service.client;


import com.owr.product_service.exceptions.InventoryUnavailableException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/*=================================================================================
 * Project: product-service
 * File: InventoryClient
 * Created by: Ochwada
 * Created on: 11, 8/11/2025, 10:46 AM
 * Description: HTTP client to communicate with inventory
 =================================================================================*/
@Component
@RequiredArgsConstructor
public class InventoryClient {

    /**
     * The HTTP client used to send requests to external services.
     */
    private final RestTemplate restTemplate;


    /**
     * The base URL of the inventory service.
     * *
     * This value is injected from the application's configuration (e.g., `application.properties` or `application.yml`)
     * using the property key {@code inventory.service.port}.
     */
    @Value("${inventory.service.port}")
    private String inventoryServiceUrl;

    // ================================= RECORD  ======================================

    /**
     * Represents a request to check or update inventory for a specific product and quantity.
     *
     * @param productId the ID of the product
     * @param quantity  the quantity of the product involved in the inventory operation
     */
    public record InventoryRequest(Long productId, int quantity) {
    }

    // ==================================================================================

    /**
     * Extracts the Authorization token from the current HTTP request and
     * builds an {@link HttpHeaders} object with required headers.
     *
     * <p>This method is typically used when forwarding requests to other services
     * while preserving the user's access token (e.g., in microservice-to-microservice communication).</p>
     *
     * @return an {@link HttpHeaders} object containing the Authorization header and content type set to JSON.
     */

    private HttpHeaders getAuthHeaders(){
        // Access the current HTTP request from the request context ( bound to the current thread)
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        // Retrieve the actual HttpServletRequest object (contains headers)
        HttpServletRequest currentRequest = attributes.getRequest();

        // Get the Authorization token from the current request's headers
        String token = currentRequest.getHeader("Authorization");

        // Create new headers with JSON content type and forward the Authorization token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        return headers;
    }

    /**
     * Retrieves the available stock quantity for a given product
     * by making an HTTP GET request to the inventory service.
     *
     * @param productId the unique identifier of the product
     * @return the stock quantity from the inventory service, or 0 if unavailable
     * @throws InventoryUnavailableException if the inventory service is unreachable or returns an error
     */
    public int getStockQuantity(Long productId) {

        // Wrap the authorization headers in an HttpEntity (no request body needed for GET)
        HttpEntity<Void> entity = new HttpEntity<>(getAuthHeaders());

        // 2. Build the full URL for the inventory lookup in a safe way:
        URI uri = UriComponentsBuilder
                .fromHttpUrl(inventoryServiceUrl)   // e.g. "http://inventory-service:8687/api/inventory/"
                .pathSegment("{id}")                // appends /{id} safely
                .build(productId);                   // replaces {id} with the actual value


        try {
        // Send GET request to inventory service: /{productId}
        ResponseEntity<Integer> response = restTemplate.exchange(
                uri, // Full URL for the product stock lookup
                HttpMethod.GET, // HTTP GET method
                entity, // Headers including Authorization
                Integer.class // Expecting an Integer response (stock quantity)
        );
        // Return the quantity if not null, otherwise default to 0
        return response.getBody() != null ? response.getBody() : 0;
        } catch (RestClientResponseException e) {
            // 5b. Thrown when the service returns a 4xx or 5xx status code.
            throw new InventoryUnavailableException(
                    "Inventory error " + e.getRawStatusCode() + " at " + uri, e
            );
        }
    }

    /**
     * Sends a POST request to the inventory service to create an inventory record
     * for the specified product with the given initial quantity.
     *
     * <p>This method is typically called right after a new product is created,
     * ensuring that its initial stock is registered in the external inventory system.</p>
     *
     * <p>The request includes authentication headers and a request body containing the product ID
     * and quantity, wrapped inside an {@link InventoryRequest} object.</p>
     *
     * @param productId the ID of the product to register in the inventory
     * @param quantity  the initial stock quantity for the product
     */
    public void createInventory(Long productId, int quantity) {

        // Wrap the inventory request and authentication headers into an HttpEntity
        HttpEntity<InventoryRequest> entity =
                new HttpEntity<>(new InventoryRequest(productId, quantity), getAuthHeaders());

        // Send a POST request to the inventory service to create the inventory record
        restTemplate.exchange(
                inventoryServiceUrl,  // The base URL of the inventory service (should handle POST requests)
                HttpMethod.POST,
                entity,               // Request body + headers
                Void.class            // No response body expected
        );
    }

}
