package com.warehouse.app.domain;

import static com.warehouse.app.domain.BatchTestSamples.*;
import static com.warehouse.app.domain.ProductTestSamples.*;
import static com.warehouse.app.domain.SupplierTestSamples.*;
import static com.warehouse.app.domain.WarehouseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.warehouse.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void warehousesTest() {
        Product product = getProductRandomSampleGenerator();
        Warehouse warehouseBack = getWarehouseRandomSampleGenerator();

        product.addWarehouses(warehouseBack);
        assertThat(product.getWarehouses()).containsOnly(warehouseBack);

        product.removeWarehouses(warehouseBack);
        assertThat(product.getWarehouses()).doesNotContain(warehouseBack);

        product.warehouses(new HashSet<>(Set.of(warehouseBack)));
        assertThat(product.getWarehouses()).containsOnly(warehouseBack);

        product.setWarehouses(new HashSet<>());
        assertThat(product.getWarehouses()).doesNotContain(warehouseBack);
    }

    @Test
    void suppliersTest() {
        Product product = getProductRandomSampleGenerator();
        Supplier supplierBack = getSupplierRandomSampleGenerator();

        product.addSuppliers(supplierBack);
        assertThat(product.getSuppliers()).containsOnly(supplierBack);
        assertThat(supplierBack.getProducts()).containsOnly(product);

        product.removeSuppliers(supplierBack);
        assertThat(product.getSuppliers()).doesNotContain(supplierBack);
        assertThat(supplierBack.getProducts()).doesNotContain(product);

        product.suppliers(new HashSet<>(Set.of(supplierBack)));
        assertThat(product.getSuppliers()).containsOnly(supplierBack);
        assertThat(supplierBack.getProducts()).containsOnly(product);

        product.setSuppliers(new HashSet<>());
        assertThat(product.getSuppliers()).doesNotContain(supplierBack);
        assertThat(supplierBack.getProducts()).doesNotContain(product);
    }

    @Test
    void batchesTest() {
        Product product = getProductRandomSampleGenerator();
        Batch batchBack = getBatchRandomSampleGenerator();

        product.addBatches(batchBack);
        assertThat(product.getBatches()).containsOnly(batchBack);
        assertThat(batchBack.getProduct()).isEqualTo(product);

        product.removeBatches(batchBack);
        assertThat(product.getBatches()).doesNotContain(batchBack);
        assertThat(batchBack.getProduct()).isNull();

        product.batches(new HashSet<>(Set.of(batchBack)));
        assertThat(product.getBatches()).containsOnly(batchBack);
        assertThat(batchBack.getProduct()).isEqualTo(product);

        product.setBatches(new HashSet<>());
        assertThat(product.getBatches()).doesNotContain(batchBack);
        assertThat(batchBack.getProduct()).isNull();
    }
}
