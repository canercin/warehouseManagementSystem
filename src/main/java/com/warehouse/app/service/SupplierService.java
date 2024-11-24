package com.warehouse.app.service;

import com.warehouse.app.service.dto.SupplierDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.warehouse.app.domain.Supplier}.
 */
public interface SupplierService {
    /**
     * Save a supplier.
     *
     * @param supplierDTO the entity to save.
     * @return the persisted entity.
     */
    SupplierDTO save(SupplierDTO supplierDTO);

    /**
     * Updates a supplier.
     *
     * @param supplierDTO the entity to update.
     * @return the persisted entity.
     */
    SupplierDTO update(SupplierDTO supplierDTO);

    /**
     * Partially updates a supplier.
     *
     * @param supplierDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SupplierDTO> partialUpdate(SupplierDTO supplierDTO);

    /**
     * Get all the suppliers.
     *
     * @return the list of entities.
     */
    List<SupplierDTO> findAll();

    /**
     * Get all the suppliers with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SupplierDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" supplier.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SupplierDTO> findOne(UUID id);

    /**
     * Delete the "id" supplier.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Get the count of supplier
     * @return count of supplier
     */
    Integer countAll();
}
