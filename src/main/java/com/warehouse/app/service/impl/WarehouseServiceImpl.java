package com.warehouse.app.service.impl;

import com.warehouse.app.domain.Warehouse;
import com.warehouse.app.repository.WarehouseRepository;
import com.warehouse.app.service.WarehouseService;
import com.warehouse.app.service.dto.WarehouseDTO;
import com.warehouse.app.service.mapper.WarehouseMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.warehouse.app.domain.Warehouse}.
 */
@Service
@Transactional
public class WarehouseServiceImpl implements WarehouseService {

    private static final Logger LOG = LoggerFactory.getLogger(WarehouseServiceImpl.class);

    private final WarehouseRepository warehouseRepository;

    private final WarehouseMapper warehouseMapper;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository, WarehouseMapper warehouseMapper) {
        this.warehouseRepository = warehouseRepository;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public WarehouseDTO save(WarehouseDTO warehouseDTO) {
        LOG.debug("Request to save Warehouse : {}", warehouseDTO);
        Warehouse warehouse = warehouseMapper.toEntity(warehouseDTO);
        warehouse = warehouseRepository.save(warehouse);
        return warehouseMapper.toDto(warehouse);
    }

    @Override
    public WarehouseDTO update(WarehouseDTO warehouseDTO) {
        LOG.debug("Request to update Warehouse : {}", warehouseDTO);
        Warehouse warehouse = warehouseMapper.toEntity(warehouseDTO);
        warehouse = warehouseRepository.save(warehouse);
        return warehouseMapper.toDto(warehouse);
    }

    @Override
    public Optional<WarehouseDTO> partialUpdate(WarehouseDTO warehouseDTO) {
        LOG.debug("Request to partially update Warehouse : {}", warehouseDTO);

        return warehouseRepository
            .findById(warehouseDTO.getId())
            .map(existingWarehouse -> {
                warehouseMapper.partialUpdate(existingWarehouse, warehouseDTO);

                return existingWarehouse;
            })
            .map(warehouseRepository::save)
            .map(warehouseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WarehouseDTO> findAll() {
        LOG.debug("Request to get all Warehouses");
        return warehouseRepository.findAll().stream().map(warehouseMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WarehouseDTO> findOne(UUID id) {
        LOG.debug("Request to get Warehouse : {}", id);
        return warehouseRepository.findById(id).map(warehouseMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete Warehouse : {}", id);
        warehouseRepository.deleteById(id);
    }

    @Override
    public Integer countAll() {
        return Math.toIntExact(warehouseRepository.count());
    }
}
