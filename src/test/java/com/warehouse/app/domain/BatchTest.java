package com.warehouse.app.domain;

import static com.warehouse.app.domain.BatchTestSamples.*;
import static com.warehouse.app.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.warehouse.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BatchTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Batch.class);
        Batch batch1 = getBatchSample1();
        Batch batch2 = new Batch();
        assertThat(batch1).isNotEqualTo(batch2);

        batch2.setId(batch1.getId());
        assertThat(batch1).isEqualTo(batch2);

        batch2 = getBatchSample2();
        assertThat(batch1).isNotEqualTo(batch2);
    }

    @Test
    void productTest() {
        Batch batch = getBatchRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        batch.setProduct(productBack);
        assertThat(batch.getProduct()).isEqualTo(productBack);

        batch.product(null);
        assertThat(batch.getProduct()).isNull();
    }
}
