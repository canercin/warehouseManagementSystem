package com.warehouse.app.web.rest;

import static com.warehouse.app.domain.SupplierContactAsserts.*;
import static com.warehouse.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.app.IntegrationTest;
import com.warehouse.app.domain.SupplierContact;
import com.warehouse.app.repository.SupplierContactRepository;
import com.warehouse.app.service.SupplierContactService;
import com.warehouse.app.service.dto.SupplierContactDTO;
import com.warehouse.app.service.mapper.SupplierContactMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
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
 * Integration tests for the {@link SupplierContactResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SupplierContactResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/supplier-contacts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SupplierContactRepository supplierContactRepository;

    @Mock
    private SupplierContactRepository supplierContactRepositoryMock;

    @Autowired
    private SupplierContactMapper supplierContactMapper;

    @Mock
    private SupplierContactService supplierContactServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSupplierContactMockMvc;

    private SupplierContact supplierContact;

    private SupplierContact insertedSupplierContact;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupplierContact createEntity() {
        return new SupplierContact().name(DEFAULT_NAME).email(DEFAULT_EMAIL).phone(DEFAULT_PHONE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupplierContact createUpdatedEntity() {
        return new SupplierContact().name(UPDATED_NAME).email(UPDATED_EMAIL).phone(UPDATED_PHONE);
    }

    @BeforeEach
    public void initTest() {
        supplierContact = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSupplierContact != null) {
            supplierContactRepository.delete(insertedSupplierContact);
            insertedSupplierContact = null;
        }
    }

    @Test
    @Transactional
    void createSupplierContact() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SupplierContact
        SupplierContactDTO supplierContactDTO = supplierContactMapper.toDto(supplierContact);
        var returnedSupplierContactDTO = om.readValue(
            restSupplierContactMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierContactDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SupplierContactDTO.class
        );

        // Validate the SupplierContact in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSupplierContact = supplierContactMapper.toEntity(returnedSupplierContactDTO);
        assertSupplierContactUpdatableFieldsEquals(returnedSupplierContact, getPersistedSupplierContact(returnedSupplierContact));

        insertedSupplierContact = returnedSupplierContact;
    }

    @Test
    @Transactional
    void createSupplierContactWithExistingId() throws Exception {
        // Create the SupplierContact with an existing ID
        supplierContact.setId(1L);
        SupplierContactDTO supplierContactDTO = supplierContactMapper.toDto(supplierContact);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupplierContactMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierContactDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SupplierContact in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSupplierContacts() throws Exception {
        // Initialize the database
        insertedSupplierContact = supplierContactRepository.saveAndFlush(supplierContact);

        // Get all the supplierContactList
        restSupplierContactMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierContact.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupplierContactsWithEagerRelationshipsIsEnabled() throws Exception {
        when(supplierContactServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierContactMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(supplierContactServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupplierContactsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(supplierContactServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierContactMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(supplierContactRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSupplierContact() throws Exception {
        // Initialize the database
        insertedSupplierContact = supplierContactRepository.saveAndFlush(supplierContact);

        // Get the supplierContact
        restSupplierContactMockMvc
            .perform(get(ENTITY_API_URL_ID, supplierContact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(supplierContact.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE));
    }

    @Test
    @Transactional
    void getNonExistingSupplierContact() throws Exception {
        // Get the supplierContact
        restSupplierContactMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSupplierContact() throws Exception {
        // Initialize the database
        insertedSupplierContact = supplierContactRepository.saveAndFlush(supplierContact);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierContact
        SupplierContact updatedSupplierContact = supplierContactRepository.findById(supplierContact.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSupplierContact are not directly saved in db
        em.detach(updatedSupplierContact);
        updatedSupplierContact.name(UPDATED_NAME).email(UPDATED_EMAIL).phone(UPDATED_PHONE);
        SupplierContactDTO supplierContactDTO = supplierContactMapper.toDto(updatedSupplierContact);

        restSupplierContactMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplierContactDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierContactDTO))
            )
            .andExpect(status().isOk());

        // Validate the SupplierContact in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSupplierContactToMatchAllProperties(updatedSupplierContact);
    }

    @Test
    @Transactional
    void putNonExistingSupplierContact() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierContact.setId(longCount.incrementAndGet());

        // Create the SupplierContact
        SupplierContactDTO supplierContactDTO = supplierContactMapper.toDto(supplierContact);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierContactMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplierContactDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierContact in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSupplierContact() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierContact.setId(longCount.incrementAndGet());

        // Create the SupplierContact
        SupplierContactDTO supplierContactDTO = supplierContactMapper.toDto(supplierContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierContactMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierContact in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSupplierContact() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierContact.setId(longCount.incrementAndGet());

        // Create the SupplierContact
        SupplierContactDTO supplierContactDTO = supplierContactMapper.toDto(supplierContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierContactMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierContactDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierContact in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSupplierContactWithPatch() throws Exception {
        // Initialize the database
        insertedSupplierContact = supplierContactRepository.saveAndFlush(supplierContact);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierContact using partial update
        SupplierContact partialUpdatedSupplierContact = new SupplierContact();
        partialUpdatedSupplierContact.setId(supplierContact.getId());

        partialUpdatedSupplierContact.name(UPDATED_NAME).email(UPDATED_EMAIL);

        restSupplierContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierContact.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplierContact))
            )
            .andExpect(status().isOk());

        // Validate the SupplierContact in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplierContactUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSupplierContact, supplierContact),
            getPersistedSupplierContact(supplierContact)
        );
    }

    @Test
    @Transactional
    void fullUpdateSupplierContactWithPatch() throws Exception {
        // Initialize the database
        insertedSupplierContact = supplierContactRepository.saveAndFlush(supplierContact);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierContact using partial update
        SupplierContact partialUpdatedSupplierContact = new SupplierContact();
        partialUpdatedSupplierContact.setId(supplierContact.getId());

        partialUpdatedSupplierContact.name(UPDATED_NAME).email(UPDATED_EMAIL).phone(UPDATED_PHONE);

        restSupplierContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierContact.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplierContact))
            )
            .andExpect(status().isOk());

        // Validate the SupplierContact in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplierContactUpdatableFieldsEquals(
            partialUpdatedSupplierContact,
            getPersistedSupplierContact(partialUpdatedSupplierContact)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSupplierContact() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierContact.setId(longCount.incrementAndGet());

        // Create the SupplierContact
        SupplierContactDTO supplierContactDTO = supplierContactMapper.toDto(supplierContact);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, supplierContactDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplierContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierContact in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSupplierContact() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierContact.setId(longCount.incrementAndGet());

        // Create the SupplierContact
        SupplierContactDTO supplierContactDTO = supplierContactMapper.toDto(supplierContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplierContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierContact in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSupplierContact() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierContact.setId(longCount.incrementAndGet());

        // Create the SupplierContact
        SupplierContactDTO supplierContactDTO = supplierContactMapper.toDto(supplierContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierContactMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(supplierContactDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierContact in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSupplierContact() throws Exception {
        // Initialize the database
        insertedSupplierContact = supplierContactRepository.saveAndFlush(supplierContact);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the supplierContact
        restSupplierContactMockMvc
            .perform(delete(ENTITY_API_URL_ID, supplierContact.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return supplierContactRepository.count();
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

    protected SupplierContact getPersistedSupplierContact(SupplierContact supplierContact) {
        return supplierContactRepository.findById(supplierContact.getId()).orElseThrow();
    }

    protected void assertPersistedSupplierContactToMatchAllProperties(SupplierContact expectedSupplierContact) {
        assertSupplierContactAllPropertiesEquals(expectedSupplierContact, getPersistedSupplierContact(expectedSupplierContact));
    }

    protected void assertPersistedSupplierContactToMatchUpdatableProperties(SupplierContact expectedSupplierContact) {
        assertSupplierContactAllUpdatablePropertiesEquals(expectedSupplierContact, getPersistedSupplierContact(expectedSupplierContact));
    }
}
