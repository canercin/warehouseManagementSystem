package com.warehouse.app.service.mapper;

import static com.warehouse.app.domain.SupplierContactAsserts.*;
import static com.warehouse.app.domain.SupplierContactTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SupplierContactMapperTest {

    private SupplierContactMapper supplierContactMapper;

    @BeforeEach
    void setUp() {
        supplierContactMapper = new SupplierContactMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSupplierContactSample1();
        var actual = supplierContactMapper.toEntity(supplierContactMapper.toDto(expected));
        assertSupplierContactAllPropertiesEquals(expected, actual);
    }
}
