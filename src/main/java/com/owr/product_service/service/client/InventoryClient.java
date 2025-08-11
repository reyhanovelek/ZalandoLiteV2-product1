package com.owr.product_service.service.client;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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


}
