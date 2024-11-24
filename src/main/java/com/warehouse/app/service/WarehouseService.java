package com.warehouse.app.service;

import com.warehouse.app.service.dto.WarehouseDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing {@link com.warehouse.app.domain.Warehouse}.
 */
public interface WarehouseService {
    /**
     * Save a warehouse.
     *
     * @param warehouseDTO the entity to save.
     * @return the persisted entity.
     */
    WarehouseDTO save(WarehouseDTO warehouseDTO);

    /**
     * Updates a warehouse.
     *
     * @param warehouseDTO the entity to update.
     * @return the persisted entity.
     */
    WarehouseDTO update(WarehouseDTO warehouseDTO);

    /**
     * Partially updates a warehouse.
     *
     * @param warehouseDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WarehouseDTO> partialUpdate(WarehouseDTO warehouseDTO);

    /**
     * Get all the warehouses.
     *
     * @return the list of entities.
     */
    List<WarehouseDTO> findAll();

    /**
     * Get the "id" warehouse.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WarehouseDTO> findOne(UUID id);

    /**
     * Delete the "id" warehouse.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    Integer countAll();
}
