package com.warehouse.app.web.rest;

import com.warehouse.app.repository.SupplierContactRepository;
import com.warehouse.app.service.AnySupplierNotFoundException;
import com.warehouse.app.service.SupplierContactService;
import com.warehouse.app.service.dto.SupplierContactDTO;
import com.warehouse.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.warehouse.app.domain.SupplierContact}.
 */
@RestController
@RequestMapping("/api/supplier-contacts")
public class SupplierContactResource {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierContactResource.class);

    private static final String ENTITY_NAME = "supplierContact";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupplierContactService supplierContactService;

    private final SupplierContactRepository supplierContactRepository;

    public SupplierContactResource(SupplierContactService supplierContactService, SupplierContactRepository supplierContactRepository) {
        this.supplierContactService = supplierContactService;
        this.supplierContactRepository = supplierContactRepository;
    }

    /**
     * {@code POST  /supplier-contacts} : Create a new supplierContact.
     *
     * @param supplierContactDTO the supplierContactDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplierContactDTO, or with status {@code 400 (Bad Request)} if the supplierContact has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SupplierContactDTO> createSupplierContact(@RequestBody SupplierContactDTO supplierContactDTO)
        throws URISyntaxException, AnySupplierNotFoundException {
        LOG.debug("REST request to save SupplierContact : {}", supplierContactDTO);
        if (supplierContactDTO.getId() != null) {
            throw new BadRequestAlertException("A new supplierContact cannot already have an ID", ENTITY_NAME, "idexists");
        }
        supplierContactDTO = supplierContactService.save(supplierContactDTO);
        return ResponseEntity.created(new URI("/api/supplier-contacts/" + supplierContactDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, supplierContactDTO.getId().toString()))
            .body(supplierContactDTO);
    }

    /**
     * {@code PUT  /supplier-contacts/:id} : Updates an existing supplierContact.
     *
     * @param id the id of the supplierContactDTO to save.
     * @param supplierContactDTO the supplierContactDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierContactDTO,
     * or with status {@code 400 (Bad Request)} if the supplierContactDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierContactDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SupplierContactDTO> updateSupplierContact(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SupplierContactDTO supplierContactDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SupplierContact : {}, {}", id, supplierContactDTO);
        if (supplierContactDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierContactDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierContactRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        supplierContactDTO = supplierContactService.update(supplierContactDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, supplierContactDTO.getId().toString()))
            .body(supplierContactDTO);
    }

    /**
     * {@code PATCH  /supplier-contacts/:id} : Partial updates given fields of an existing supplierContact, field will ignore if it is null
     *
     * @param id the id of the supplierContactDTO to save.
     * @param supplierContactDTO the supplierContactDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierContactDTO,
     * or with status {@code 400 (Bad Request)} if the supplierContactDTO is not valid,
     * or with status {@code 404 (Not Found)} if the supplierContactDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the supplierContactDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SupplierContactDTO> partialUpdateSupplierContact(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SupplierContactDTO supplierContactDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SupplierContact partially : {}, {}", id, supplierContactDTO);
        if (supplierContactDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierContactDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierContactRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SupplierContactDTO> result = supplierContactService.partialUpdate(supplierContactDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, supplierContactDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /supplier-contacts} : get all the supplierContacts.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supplierContacts in body.
     */
    @GetMapping("")
    public List<SupplierContactDTO> getAllSupplierContacts(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all SupplierContacts");
        return supplierContactService.findAll();
    }

    /**
     * {@code GET  /supplier-contacts/:id} : get the "id" supplierContact.
     *
     * @param id the id of the supplierContactDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplierContactDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SupplierContactDTO> getSupplierContact(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SupplierContact : {}", id);
        Optional<SupplierContactDTO> supplierContactDTO = supplierContactService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplierContactDTO);
    }

    /**
     * {@code DELETE  /supplier-contacts/:id} : delete the "id" supplierContact.
     *
     * @param id the id of the supplierContactDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplierContact(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SupplierContact : {}", id);
        supplierContactService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
