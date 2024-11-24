package com.warehouse.app.domain;

import static com.warehouse.app.domain.SupplierContactTestSamples.*;
import static com.warehouse.app.domain.SupplierTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.warehouse.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SupplierContactTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierContact.class);
        SupplierContact supplierContact1 = getSupplierContactSample1();
        SupplierContact supplierContact2 = new SupplierContact();
        assertThat(supplierContact1).isNotEqualTo(supplierContact2);

        supplierContact2.setId(supplierContact1.getId());
        assertThat(supplierContact1).isEqualTo(supplierContact2);

        supplierContact2 = getSupplierContactSample2();
        assertThat(supplierContact1).isNotEqualTo(supplierContact2);
    }

    @Test
    void supplierTest() {
        SupplierContact supplierContact = getSupplierContactRandomSampleGenerator();
        Supplier supplierBack = getSupplierRandomSampleGenerator();

        supplierContact.setSupplier(supplierBack);
        assertThat(supplierContact.getSupplier()).isEqualTo(supplierBack);

        supplierContact.supplier(null);
        assertThat(supplierContact.getSupplier()).isNull();
    }
}
