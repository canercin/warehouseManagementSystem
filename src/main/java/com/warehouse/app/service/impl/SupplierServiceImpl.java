package com.warehouse.app.service.impl;

import com.warehouse.app.domain.Supplier;
import com.warehouse.app.repository.SupplierRepository;
import com.warehouse.app.service.SupplierService;
import com.warehouse.app.service.dto.SupplierDTO;
import com.warehouse.app.service.mapper.SupplierMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.warehouse.app.domain.Supplier}.
 */
@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierServiceImpl.class);

    private final SupplierRepository supplierRepository;

    private final SupplierMapper supplierMapper;

    public SupplierServiceImpl(SupplierRepository supplierRepository, SupplierMapper supplierMapper) {
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
    }

    @Override
    public SupplierDTO save(SupplierDTO supplierDTO) {
        LOG.debug("Request to save Supplier : {}", supplierDTO);
        Supplier supplier = supplierMapper.toEntity(supplierDTO);
        supplier = supplierRepository.save(supplier);
        return supplierMapper.toDto(supplier);
    }

    @Override
    public SupplierDTO update(SupplierDTO supplierDTO) {
        LOG.debug("Request to update Supplier : {}", supplierDTO);
        Supplier supplier = supplierMapper.toEntity(supplierDTO);
        supplier = supplierRepository.save(supplier);
        return supplierMapper.toDto(supplier);
    }

    @Override
    public Optional<SupplierDTO> partialUpdate(SupplierDTO supplierDTO) {
        LOG.debug("Request to partially update Supplier : {}", supplierDTO);

        return supplierRepository
            .findById(supplierDTO.getId())
            .map(existingSupplier -> {
                supplierMapper.partialUpdate(existingSupplier, supplierDTO);

                return existingSupplier;
            })
            .map(supplierRepository::save)
            .map(supplierMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierDTO> findAll() {
        LOG.debug("Request to get all Suppliers");
        return supplierRepository.findAll().stream().map(supplierMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<SupplierDTO> findAllWithEagerRelationships(Pageable pageable) {
        return supplierRepository.findAllWithEagerRelationships(pageable).map(supplierMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SupplierDTO> findOne(UUID id) {
        LOG.debug("Request to get Supplier : {}", id);
        return supplierRepository.findOneWithEagerRelationships(id).map(supplierMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete Supplier : {}", id);
        supplierRepository.deleteById(id);
    }

    @Override
    public Integer countAll() {
        return Math.toIntExact(supplierRepository.count());
    }
}
