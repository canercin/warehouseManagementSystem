package com.warehouse.app.jobs;

import com.warehouse.app.service.ProductService;
import com.warehouse.app.service.dto.ProductDTO;
import com.warehouse.app.service.mapper.ProductMapper;
import org.jobrunr.jobs.annotations.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JobsOfProduct {

    private final ProductService productService;
    private final ProductMapper productMapper;
    private static final Logger LOG = LoggerFactory.getLogger(JobsOfProduct.class);

    public JobsOfProduct(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @Job(name = "Copy Product Adder")
    public void copyProductAdder(ProductDTO productDTO) {
        LOG.info("Job copyProductAdder started");
        productDTO.setId(null);
        productDTO.setName("Copy of " + productDTO.getName());
        productService.save(productDTO);
        LOG.info("Job copyProductAdder finished");
    }
}
