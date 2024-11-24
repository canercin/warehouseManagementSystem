package com.warehouse.app.domain;

import static com.warehouse.app.domain.ProductTestSamples.*;
import static com.warehouse.app.domain.SupplierContactTestSamples.*;
import static com.warehouse.app.domain.SupplierTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.warehouse.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SupplierTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Supplier.class);
        Supplier supplier1 = getSupplierSample1();
        Supplier supplier2 = new Supplier();
        assertThat(supplier1).isNotEqualTo(supplier2);

        supplier2.setId(supplier1.getId());
        assertThat(supplier1).isEqualTo(supplier2);

        supplier2 = getSupplierSample2();
        assertThat(supplier1).isNotEqualTo(supplier2);
    }

    @Test
    void productsTest() {
        Supplier supplier = getSupplierRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        supplier.addProducts(productBack);
        assertThat(supplier.getProducts()).containsOnly(productBack);

        supplier.removeProducts(productBack);
        assertThat(supplier.getProducts()).doesNotContain(productBack);

        supplier.products(new HashSet<>(Set.of(productBack)));
        assertThat(supplier.getProducts()).containsOnly(productBack);

        supplier.setProducts(new HashSet<>());
        assertThat(supplier.getProducts()).doesNotContain(productBack);
    }

    @Test
    void contactsTest() {
        Supplier supplier = getSupplierRandomSampleGenerator();
        SupplierContact supplierContactBack = getSupplierContactRandomSampleGenerator();

        supplier.addContacts(supplierContactBack);
        assertThat(supplier.getContacts()).containsOnly(supplierContactBack);
        assertThat(supplierContactBack.getSupplier()).isEqualTo(supplier);

        supplier.removeContacts(supplierContactBack);
        assertThat(supplier.getContacts()).doesNotContain(supplierContactBack);
        assertThat(supplierContactBack.getSupplier()).isNull();

        supplier.contacts(new HashSet<>(Set.of(supplierContactBack)));
        assertThat(supplier.getContacts()).containsOnly(supplierContactBack);
        assertThat(supplierContactBack.getSupplier()).isEqualTo(supplier);

        supplier.setContacts(new HashSet<>());
        assertThat(supplier.getContacts()).doesNotContain(supplierContactBack);
        assertThat(supplierContactBack.getSupplier()).isNull();
    }
}
