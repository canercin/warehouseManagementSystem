package com.warehouse.app.service.impl;

import com.warehouse.app.domain.SupplierContact;
import com.warehouse.app.repository.SupplierContactRepository;
import com.warehouse.app.service.AnySupplierNotFoundException;
import com.warehouse.app.service.SupplierContactService;
import com.warehouse.app.service.SupplierService;
import com.warehouse.app.service.dto.SupplierContactDTO;
import com.warehouse.app.service.mapper.SupplierContactMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.warehouse.app.domain.SupplierContact}.
 */
@Service
@Transactional
public class SupplierContactServiceImpl implements SupplierContactService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierContactServiceImpl.class);

    private final SupplierContactRepository supplierContactRepository;

    private final SupplierContactMapper supplierContactMapper;

    private final SupplierService supplierService;

    public SupplierContactServiceImpl(SupplierContactRepository supplierContactRepository, SupplierContactMapper supplierContactMapper, SupplierService supplierService) {
        this.supplierContactRepository = supplierContactRepository;
        this.supplierContactMapper = supplierContactMapper;
        this.supplierService = supplierService;
    }

    @Override
    public SupplierContactDTO save(SupplierContactDTO supplierContactDTO) throws AnySupplierNotFoundException {
        LOG.debug("Request to save SupplierContact : {}", supplierContactDTO);
        if (supplierService.countAll().equals(0)){
            LOG.error("There are no suppliers in the database");
            throw new AnySupplierNotFoundException("There are no suppliers in the database");
        }
        SupplierContact supplierContact = supplierContactMapper.toEntity(supplierContactDTO);
        supplierContact = supplierContactRepository.save(supplierContact);
        return supplierContactMapper.toDto(supplierContact);
    }

    @Override
    public SupplierContactDTO update(SupplierContactDTO supplierContactDTO) {
        LOG.debug("Request to update SupplierContact : {}", supplierContactDTO);
        SupplierContact supplierContact = supplierContactMapper.toEntity(supplierContactDTO);
        supplierContact = supplierContactRepository.save(supplierContact);
        return supplierContactMapper.toDto(supplierContact);
    }

    @Override
    public Optional<SupplierContactDTO> partialUpdate(SupplierContactDTO supplierContactDTO) {
        LOG.debug("Request to partially update SupplierContact : {}", supplierContactDTO);

        return supplierContactRepository
            .findById(supplierContactDTO.getId())
            .map(existingSupplierContact -> {
                supplierContactMapper.partialUpdate(existingSupplierContact, supplierContactDTO);

                return existingSupplierContact;
            })
            .map(supplierContactRepository::save)
            .map(supplierContactMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierContactDTO> findAll() {
        LOG.debug("Request to get all SupplierContacts");
        return supplierContactRepository
            .findAll()
            .stream()
            .map(supplierContactMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<SupplierContactDTO> findAllWithEagerRelationships(Pageable pageable) {
        return supplierContactRepository.findAllWithEagerRelationships(pageable).map(supplierContactMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SupplierContactDTO> findOne(Long id) {
        LOG.debug("Request to get SupplierContact : {}", id);
        return supplierContactRepository.findOneWithEagerRelationships(id).map(supplierContactMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierContact : {}", id);
        supplierContactRepository.deleteById(id);
    }
}
