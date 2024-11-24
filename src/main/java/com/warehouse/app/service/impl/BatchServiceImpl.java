package com.warehouse.app.service.impl;

import com.warehouse.app.domain.Batch;
import com.warehouse.app.repository.BatchRepository;
import com.warehouse.app.service.BatchService;
import com.warehouse.app.service.ProductNotFoundException;
import com.warehouse.app.service.ProductService;
import com.warehouse.app.service.dto.BatchDTO;
import com.warehouse.app.service.mapper.BatchMapper;
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
 * Service Implementation for managing {@link com.warehouse.app.domain.Batch}.
 */
@Service
@Transactional
public class BatchServiceImpl implements BatchService {

    private static final Logger LOG = LoggerFactory.getLogger(BatchServiceImpl.class);

    private final BatchRepository batchRepository;

    private final BatchMapper batchMapper;

    private final ProductService productService;

    public BatchServiceImpl(BatchRepository batchRepository, BatchMapper batchMapper, ProductService productService) {
        this.batchRepository = batchRepository;
        this.batchMapper = batchMapper;
        this.productService = productService;
    }

    @Override
    public BatchDTO save(BatchDTO batchDTO) throws ProductNotFoundException {
        LOG.debug("Request to save Batch : {}", batchDTO);
        if (productService.countAll().equals(0)){
            LOG.error("No product found in the system");
            throw new ProductNotFoundException("No product found in the system");
        }
        Batch batch = batchMapper.toEntity(batchDTO);
        batch = batchRepository.save(batch);
        return batchMapper.toDto(batch);
    }

    @Override
    public BatchDTO update(BatchDTO batchDTO) {
        LOG.debug("Request to update Batch : {}", batchDTO);
        Batch batch = batchMapper.toEntity(batchDTO);
        batch = batchRepository.save(batch);
        return batchMapper.toDto(batch);
    }

    @Override
    public Optional<BatchDTO> partialUpdate(BatchDTO batchDTO) {
        LOG.debug("Request to partially update Batch : {}", batchDTO);

        return batchRepository
            .findById(batchDTO.getId())
            .map(existingBatch -> {
                batchMapper.partialUpdate(existingBatch, batchDTO);

                return existingBatch;
            })
            .map(batchRepository::save)
            .map(batchMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BatchDTO> findAll() {
        LOG.debug("Request to get all Batches");
        return batchRepository.findAll().stream().map(batchMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<BatchDTO> findAllWithEagerRelationships(Pageable pageable) {
        return batchRepository.findAllWithEagerRelationships(pageable).map(batchMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BatchDTO> findOne(UUID id) {
        LOG.debug("Request to get Batch : {}", id);
        return batchRepository.findOneWithEagerRelationships(id).map(batchMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete Batch : {}", id);
        batchRepository.deleteById(id);
    }
}
