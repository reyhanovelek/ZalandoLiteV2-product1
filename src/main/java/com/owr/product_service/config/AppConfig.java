package com.owr.product_service.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/*=================================================================================
 * Project: product-service
 * File: AppConfig
 * Created by: Ochwada
 * Created on: 11, 8/11/2025, 10:41 AM
 * Description: Define and expose reusable Spring-managed components that can be injected  into the application
 =================================================================================*/
@Configuration
public class AppConfig {
    /**
     * Creates and registers a {@link RestTemplate} bean.
     *
     * @return a new instance of {@code RestTemplate}
     */
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
