package com.warehouse.app.service.impl;

import com.warehouse.app.domain.Product;
import com.warehouse.app.repository.ProductRepository;
import com.warehouse.app.service.ProductService;
import com.warehouse.app.service.SupplierService;
import com.warehouse.app.service.WarehouseService;
import com.warehouse.app.service.dto.ProductDTO;
import com.warehouse.app.service.mapper.ProductMapper;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.warehouse.app.service.WarehouseAndSupplierNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.warehouse.app.domain.Product}.
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final SupplierService supplierService;

    private final WarehouseService warehouseService;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, SupplierService supplierService, WarehouseService warehouseService) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.supplierService = supplierService;
        this.warehouseService = warehouseService;
    }

    @Override
    public ProductDTO save(ProductDTO productDTO) throws WarehouseAndSupplierNotFoundException {
        LOG.debug("Request to save Product : {}", productDTO);
        if (warehouseService.countAll().equals(0) && supplierService.countAll().equals(0)) {
            LOG.error("No warehouse and supplier found");
            throw new WarehouseAndSupplierNotFoundException("No warehouse and supplier found");
        }
        Product product = productMapper.toEntity(productDTO);
        product.setCreatedAt(LocalDate.now());
        product = productRepository.save(product);
        return productMapper.toDto(product);
    }

    @Override
    public ProductDTO update(ProductDTO productDTO) {
        LOG.debug("Request to update Product : {}", productDTO);
        Product product = productMapper.toEntity(productDTO);
        product = productRepository.save(product);
        return productMapper.toDto(product);
    }

    @Override
    public Optional<ProductDTO> partialUpdate(ProductDTO productDTO) {
        LOG.debug("Request to partially update Product : {}", productDTO);

        return productRepository
            .findById(productDTO.getId())
            .map(existingProduct -> {
                productMapper.partialUpdate(existingProduct, productDTO);

                return existingProduct;
            })
            .map(productRepository::save)
            .map(productMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        LOG.debug("Request to get all Products");
        return productRepository.findAll().stream().map(productMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<ProductDTO> findAllWithEagerRelationships(Pageable pageable) {
        return productRepository.findAllWithEagerRelationships(pageable).map(productMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDTO> findOne(UUID id) {
        LOG.debug("Request to get Product : {}", id);
        return productRepository.findOneWithEagerRelationships(id).map(productMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
    }

    @Override
    public Integer countAll() {
        return Math.toIntExact(productRepository.count());
    }
}
