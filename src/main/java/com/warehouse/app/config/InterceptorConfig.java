package com.warehouse.app.config;

import com.warehouse.app.interceptors.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer{
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ProductInterceptor()).addPathPatterns("/api/products");
        registry.addInterceptor(new BatchInterceptor()).addPathPatterns("/api/batches");
        registry.addInterceptor(new SupplierInterceptor()).addPathPatterns("/api/suppliers");
        registry.addInterceptor(new SupplierContactInterceptor()).addPathPatterns("/api/supplier-contacts");
        registry.addInterceptor(new WarehouseInterceptor()).addPathPatterns("/api/warehouses");
    }
}
