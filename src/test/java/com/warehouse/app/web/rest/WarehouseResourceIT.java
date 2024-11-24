package com.warehouse.app.web.rest;

import static com.warehouse.app.domain.WarehouseAsserts.*;
import static com.warehouse.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.app.IntegrationTest;
import com.warehouse.app.domain.Warehouse;
import com.warehouse.app.repository.WarehouseRepository;
import com.warehouse.app.service.dto.WarehouseDTO;
import com.warehouse.app.service.mapper.WarehouseMapper;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WarehouseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WarehouseResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/warehouses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWarehouseMockMvc;

    private Warehouse warehouse;

    private Warehouse insertedWarehouse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Warehouse createEntity() {
        return new Warehouse().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Warehouse createUpdatedEntity() {
        return new Warehouse().name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        warehouse = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedWarehouse != null) {
            warehouseRepository.delete(insertedWarehouse);
            insertedWarehouse = null;
        }
    }

    @Test
    @Transactional
    void createWarehouse() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Warehouse
        WarehouseDTO warehouseDTO = warehouseMapper.toDto(warehouse);
        var returnedWarehouseDTO = om.readValue(
            restWarehouseMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(warehouseDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WarehouseDTO.class
        );

        // Validate the Warehouse in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWarehouse = warehouseMapper.toEntity(returnedWarehouseDTO);
        assertWarehouseUpdatableFieldsEquals(returnedWarehouse, getPersistedWarehouse(returnedWarehouse));

        insertedWarehouse = returnedWarehouse;
    }

    @Test
    @Transactional
    void createWarehouseWithExistingId() throws Exception {
        // Create the Warehouse with an existing ID
        insertedWarehouse = warehouseRepository.saveAndFlush(warehouse);
        WarehouseDTO warehouseDTO = warehouseMapper.toDto(warehouse);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWarehouseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(warehouseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Warehouse in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWarehouses() throws Exception {
        // Initialize the database
        insertedWarehouse = warehouseRepository.saveAndFlush(warehouse);

        // Get all the warehouseList
        restWarehouseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(warehouse.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getWarehouse() throws Exception {
        // Initialize the database
        insertedWarehouse = warehouseRepository.saveAndFlush(warehouse);

        // Get the warehouse
        restWarehouseMockMvc
            .perform(get(ENTITY_API_URL_ID, warehouse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(warehouse.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingWarehouse() throws Exception {
        // Get the warehouse
        restWarehouseMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWarehouse() throws Exception {
        // Initialize the database
        insertedWarehouse = warehouseRepository.saveAndFlush(warehouse);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the warehouse
        Warehouse updatedWarehouse = warehouseRepository.findById(warehouse.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWarehouse are not directly saved in db
        em.detach(updatedWarehouse);
        updatedWarehouse.name(UPDATED_NAME);
        WarehouseDTO warehouseDTO = warehouseMapper.toDto(updatedWarehouse);

        restWarehouseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, warehouseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(warehouseDTO))
            )
            .andExpect(status().isOk());

        // Validate the Warehouse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWarehouseToMatchAllProperties(updatedWarehouse);
    }

    @Test
    @Transactional
    void putNonExistingWarehouse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        warehouse.setId(UUID.randomUUID());

        // Create the Warehouse
        WarehouseDTO warehouseDTO = warehouseMapper.toDto(warehouse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWarehouseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, warehouseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(warehouseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Warehouse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWarehouse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        warehouse.setId(UUID.randomUUID());

        // Create the Warehouse
        WarehouseDTO warehouseDTO = warehouseMapper.toDto(warehouse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarehouseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(warehouseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Warehouse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWarehouse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        warehouse.setId(UUID.randomUUID());

        // Create the Warehouse
        WarehouseDTO warehouseDTO = warehouseMapper.toDto(warehouse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarehouseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(warehouseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Warehouse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWarehouseWithPatch() throws Exception {
        // Initialize the database
        insertedWarehouse = warehouseRepository.saveAndFlush(warehouse);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the warehouse using partial update
        Warehouse partialUpdatedWarehouse = new Warehouse();
        partialUpdatedWarehouse.setId(warehouse.getId());

        partialUpdatedWarehouse.name(UPDATED_NAME);

        restWarehouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWarehouse.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWarehouse))
            )
            .andExpect(status().isOk());

        // Validate the Warehouse in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWarehouseUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWarehouse, warehouse),
            getPersistedWarehouse(warehouse)
        );
    }

    @Test
    @Transactional
    void fullUpdateWarehouseWithPatch() throws Exception {
        // Initialize the database
        insertedWarehouse = warehouseRepository.saveAndFlush(warehouse);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the warehouse using partial update
        Warehouse partialUpdatedWarehouse = new Warehouse();
        partialUpdatedWarehouse.setId(warehouse.getId());

        partialUpdatedWarehouse.name(UPDATED_NAME);

        restWarehouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWarehouse.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWarehouse))
            )
            .andExpect(status().isOk());

        // Validate the Warehouse in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWarehouseUpdatableFieldsEquals(partialUpdatedWarehouse, getPersistedWarehouse(partialUpdatedWarehouse));
    }

    @Test
    @Transactional
    void patchNonExistingWarehouse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        warehouse.setId(UUID.randomUUID());

        // Create the Warehouse
        WarehouseDTO warehouseDTO = warehouseMapper.toDto(warehouse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWarehouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, warehouseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(warehouseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Warehouse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWarehouse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        warehouse.setId(UUID.randomUUID());

        // Create the Warehouse
        WarehouseDTO warehouseDTO = warehouseMapper.toDto(warehouse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarehouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(warehouseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Warehouse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWarehouse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        warehouse.setId(UUID.randomUUID());

        // Create the Warehouse
        WarehouseDTO warehouseDTO = warehouseMapper.toDto(warehouse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarehouseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(warehouseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Warehouse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWarehouse() throws Exception {
        // Initialize the database
        insertedWarehouse = warehouseRepository.saveAndFlush(warehouse);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the warehouse
        restWarehouseMockMvc
            .perform(delete(ENTITY_API_URL_ID, warehouse.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return warehouseRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Warehouse getPersistedWarehouse(Warehouse warehouse) {
        return warehouseRepository.findById(warehouse.getId()).orElseThrow();
    }

    protected void assertPersistedWarehouseToMatchAllProperties(Warehouse expectedWarehouse) {
        assertWarehouseAllPropertiesEquals(expectedWarehouse, getPersistedWarehouse(expectedWarehouse));
    }

    protected void assertPersistedWarehouseToMatchUpdatableProperties(Warehouse expectedWarehouse) {
        assertWarehouseAllUpdatablePropertiesEquals(expectedWarehouse, getPersistedWarehouse(expectedWarehouse));
    }
}
