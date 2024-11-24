package com.warehouse.app.web.rest;

import static com.warehouse.app.domain.BatchAsserts.*;
import static com.warehouse.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.app.IntegrationTest;
import com.warehouse.app.domain.Batch;
import com.warehouse.app.repository.BatchRepository;
import com.warehouse.app.service.BatchService;
import com.warehouse.app.service.dto.BatchDTO;
import com.warehouse.app.service.mapper.BatchMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BatchResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BatchResourceIT {

    private static final Integer DEFAULT_BATCH_NUMBER = 1;
    private static final Integer UPDATED_BATCH_NUMBER = 2;

    private static final Integer DEFAULT_PURCHASE_QUANTITY = 1;
    private static final Integer UPDATED_PURCHASE_QUANTITY = 2;

    private static final Float DEFAULT_PURCHASE_PRICE = 1F;
    private static final Float UPDATED_PURCHASE_PRICE = 2F;

    private static final Float DEFAULT_SALE_PRICE = 1F;
    private static final Float UPDATED_SALE_PRICE = 2F;

    private static final LocalDate DEFAULT_PURCHASE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PURCHASE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/batches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BatchRepository batchRepository;

    @Mock
    private BatchRepository batchRepositoryMock;

    @Autowired
    private BatchMapper batchMapper;

    @Mock
    private BatchService batchServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBatchMockMvc;

    private Batch batch;

    private Batch insertedBatch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Batch createEntity() {
        return new Batch()
            .batchNumber(DEFAULT_BATCH_NUMBER)
            .purchaseQuantity(DEFAULT_PURCHASE_QUANTITY)
            .purchasePrice(DEFAULT_PURCHASE_PRICE)
            .salePrice(DEFAULT_SALE_PRICE)
            .purchaseDate(DEFAULT_PURCHASE_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Batch createUpdatedEntity() {
        return new Batch()
            .batchNumber(UPDATED_BATCH_NUMBER)
            .purchaseQuantity(UPDATED_PURCHASE_QUANTITY)
            .purchasePrice(UPDATED_PURCHASE_PRICE)
            .salePrice(UPDATED_SALE_PRICE)
            .purchaseDate(UPDATED_PURCHASE_DATE);
    }

    @BeforeEach
    public void initTest() {
        batch = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedBatch != null) {
            batchRepository.delete(insertedBatch);
            insertedBatch = null;
        }
    }

    @Test
    @Transactional
    void createBatch() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Batch
        BatchDTO batchDTO = batchMapper.toDto(batch);
        var returnedBatchDTO = om.readValue(
            restBatchMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(batchDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BatchDTO.class
        );

        // Validate the Batch in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBatch = batchMapper.toEntity(returnedBatchDTO);
        assertBatchUpdatableFieldsEquals(returnedBatch, getPersistedBatch(returnedBatch));

        insertedBatch = returnedBatch;
    }

    @Test
    @Transactional
    void createBatchWithExistingId() throws Exception {
        // Create the Batch with an existing ID
        insertedBatch = batchRepository.saveAndFlush(batch);
        BatchDTO batchDTO = batchMapper.toDto(batch);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(batchDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Batch in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBatches() throws Exception {
        // Initialize the database
        insertedBatch = batchRepository.saveAndFlush(batch);

        // Get all the batchList
        restBatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(batch.getId().toString())))
            .andExpect(jsonPath("$.[*].batchNumber").value(hasItem(DEFAULT_BATCH_NUMBER)))
            .andExpect(jsonPath("$.[*].purchaseQuantity").value(hasItem(DEFAULT_PURCHASE_QUANTITY)))
            .andExpect(jsonPath("$.[*].purchasePrice").value(hasItem(DEFAULT_PURCHASE_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].salePrice").value(hasItem(DEFAULT_SALE_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(DEFAULT_PURCHASE_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBatchesWithEagerRelationshipsIsEnabled() throws Exception {
        when(batchServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBatchMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(batchServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBatchesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(batchServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBatchMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(batchRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBatch() throws Exception {
        // Initialize the database
        insertedBatch = batchRepository.saveAndFlush(batch);

        // Get the batch
        restBatchMockMvc
            .perform(get(ENTITY_API_URL_ID, batch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(batch.getId().toString()))
            .andExpect(jsonPath("$.batchNumber").value(DEFAULT_BATCH_NUMBER))
            .andExpect(jsonPath("$.purchaseQuantity").value(DEFAULT_PURCHASE_QUANTITY))
            .andExpect(jsonPath("$.purchasePrice").value(DEFAULT_PURCHASE_PRICE.doubleValue()))
            .andExpect(jsonPath("$.salePrice").value(DEFAULT_SALE_PRICE.doubleValue()))
            .andExpect(jsonPath("$.purchaseDate").value(DEFAULT_PURCHASE_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBatch() throws Exception {
        // Get the batch
        restBatchMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBatch() throws Exception {
        // Initialize the database
        insertedBatch = batchRepository.saveAndFlush(batch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the batch
        Batch updatedBatch = batchRepository.findById(batch.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBatch are not directly saved in db
        em.detach(updatedBatch);
        updatedBatch
            .batchNumber(UPDATED_BATCH_NUMBER)
            .purchaseQuantity(UPDATED_PURCHASE_QUANTITY)
            .purchasePrice(UPDATED_PURCHASE_PRICE)
            .salePrice(UPDATED_SALE_PRICE)
            .purchaseDate(UPDATED_PURCHASE_DATE);
        BatchDTO batchDTO = batchMapper.toDto(updatedBatch);

        restBatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, batchDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(batchDTO))
            )
            .andExpect(status().isOk());

        // Validate the Batch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBatchToMatchAllProperties(updatedBatch);
    }

    @Test
    @Transactional
    void putNonExistingBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        batch.setId(UUID.randomUUID());

        // Create the Batch
        BatchDTO batchDTO = batchMapper.toDto(batch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, batchDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(batchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Batch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        batch.setId(UUID.randomUUID());

        // Create the Batch
        BatchDTO batchDTO = batchMapper.toDto(batch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(batchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Batch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        batch.setId(UUID.randomUUID());

        // Create the Batch
        BatchDTO batchDTO = batchMapper.toDto(batch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatchMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(batchDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Batch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBatchWithPatch() throws Exception {
        // Initialize the database
        insertedBatch = batchRepository.saveAndFlush(batch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the batch using partial update
        Batch partialUpdatedBatch = new Batch();
        partialUpdatedBatch.setId(batch.getId());

        partialUpdatedBatch.batchNumber(UPDATED_BATCH_NUMBER).purchasePrice(UPDATED_PURCHASE_PRICE).salePrice(UPDATED_SALE_PRICE);

        restBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBatch))
            )
            .andExpect(status().isOk());

        // Validate the Batch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBatchUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBatch, batch), getPersistedBatch(batch));
    }

    @Test
    @Transactional
    void fullUpdateBatchWithPatch() throws Exception {
        // Initialize the database
        insertedBatch = batchRepository.saveAndFlush(batch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the batch using partial update
        Batch partialUpdatedBatch = new Batch();
        partialUpdatedBatch.setId(batch.getId());

        partialUpdatedBatch
            .batchNumber(UPDATED_BATCH_NUMBER)
            .purchaseQuantity(UPDATED_PURCHASE_QUANTITY)
            .purchasePrice(UPDATED_PURCHASE_PRICE)
            .salePrice(UPDATED_SALE_PRICE)
            .purchaseDate(UPDATED_PURCHASE_DATE);

        restBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBatch))
            )
            .andExpect(status().isOk());

        // Validate the Batch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBatchUpdatableFieldsEquals(partialUpdatedBatch, getPersistedBatch(partialUpdatedBatch));
    }

    @Test
    @Transactional
    void patchNonExistingBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        batch.setId(UUID.randomUUID());

        // Create the Batch
        BatchDTO batchDTO = batchMapper.toDto(batch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, batchDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(batchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Batch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        batch.setId(UUID.randomUUID());

        // Create the Batch
        BatchDTO batchDTO = batchMapper.toDto(batch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(batchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Batch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        batch.setId(UUID.randomUUID());

        // Create the Batch
        BatchDTO batchDTO = batchMapper.toDto(batch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatchMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(batchDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Batch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBatch() throws Exception {
        // Initialize the database
        insertedBatch = batchRepository.saveAndFlush(batch);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the batch
        restBatchMockMvc
            .perform(delete(ENTITY_API_URL_ID, batch.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return batchRepository.count();
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

    protected Batch getPersistedBatch(Batch batch) {
        return batchRepository.findById(batch.getId()).orElseThrow();
    }

    protected void assertPersistedBatchToMatchAllProperties(Batch expectedBatch) {
        assertBatchAllPropertiesEquals(expectedBatch, getPersistedBatch(expectedBatch));
    }

    protected void assertPersistedBatchToMatchUpdatableProperties(Batch expectedBatch) {
        assertBatchAllUpdatablePropertiesEquals(expectedBatch, getPersistedBatch(expectedBatch));
    }
}
