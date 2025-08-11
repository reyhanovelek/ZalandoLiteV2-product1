package com.owr.product_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/*=================================================================================
 * Project: product-service
 * File: InventoryUnavailableException
 * Created by: Ochwada
 * Created on: 11, 8/11/2025, 11:17 AM
 * Description: 
 =================================================================================*/
@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class InventoryUnavailableException extends RuntimeException {
    public InventoryUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
