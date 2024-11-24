package com.warehouse.app.service.mapper;

import static com.warehouse.app.domain.BatchAsserts.*;
import static com.warehouse.app.domain.BatchTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BatchMapperTest {

    private BatchMapper batchMapper;

    @BeforeEach
    void setUp() {
        batchMapper = new BatchMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBatchSample1();
        var actual = batchMapper.toEntity(batchMapper.toDto(expected));
        assertBatchAllPropertiesEquals(expected, actual);
    }
}
