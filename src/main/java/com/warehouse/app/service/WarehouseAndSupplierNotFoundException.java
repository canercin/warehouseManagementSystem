package com.warehouse.app.service;

public class WarehouseAndSupplierNotFoundException extends RuntimeException {
    public WarehouseAndSupplierNotFoundException(String message) {
        super(message);
    }
}
