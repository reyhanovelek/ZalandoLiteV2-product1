package com.owr.product_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Defines the security filter chain for HTTP requests.
     *
     * @param httpSecurity HttpSecurity instance used to configure security rules.
     * @return A built SecurityFilterChain bean.
     * @throws Exception in case of configuration errors.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(auth -> auth
                        // Uncomment the line below to allow public access to /home endpoint
                        //.requestMatchers("/home").permitAll()

                        // Require authentication for all other endpoints
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(
                        oauth2 -> oauth2
                                .jwt(jwtConfigurer -> {
                                    // Use default JWT decoder based on spring.security.oauth2.resourceserver.jwt.issuer-uri
                                    // You can add a custom converter/decoder here if needed
                                })
                );

        // Finalise and return the filter chain
        return httpSecurity.build();  // returns SecurityFilterChain object and registers it with Spring.
    }
}

