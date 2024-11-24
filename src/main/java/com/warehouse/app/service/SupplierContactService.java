package com.warehouse.app.service;

import com.warehouse.app.service.dto.SupplierContactDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.warehouse.app.domain.SupplierContact}.
 */
public interface SupplierContactService {
    /**
     * Save a supplierContact.
     *
     * @param supplierContactDTO the entity to save.
     * @return the persisted entity.
     */
    SupplierContactDTO save(SupplierContactDTO supplierContactDTO) throws AnySupplierNotFoundException;

    /**
     * Updates a supplierContact.
     *
     * @param supplierContactDTO the entity to update.
     * @return the persisted entity.
     */
    SupplierContactDTO update(SupplierContactDTO supplierContactDTO);

    /**
     * Partially updates a supplierContact.
     *
     * @param supplierContactDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SupplierContactDTO> partialUpdate(SupplierContactDTO supplierContactDTO);

    /**
     * Get all the supplierContacts.
     *
     * @return the list of entities.
     */
    List<SupplierContactDTO> findAll();

    /**
     * Get all the supplierContacts with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SupplierContactDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" supplierContact.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SupplierContactDTO> findOne(Long id);

    /**
     * Delete the "id" supplierContact.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
