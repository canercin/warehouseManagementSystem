package com.warehouse.app.domain;

import static com.warehouse.app.domain.ProductTestSamples.*;
import static com.warehouse.app.domain.WarehouseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.warehouse.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class WarehouseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Warehouse.class);
        Warehouse warehouse1 = getWarehouseSample1();
        Warehouse warehouse2 = new Warehouse();
        assertThat(warehouse1).isNotEqualTo(warehouse2);

        warehouse2.setId(warehouse1.getId());
        assertThat(warehouse1).isEqualTo(warehouse2);

        warehouse2 = getWarehouseSample2();
        assertThat(warehouse1).isNotEqualTo(warehouse2);
    }

    @Test
    void productsTest() {
        Warehouse warehouse = getWarehouseRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        warehouse.addProducts(productBack);
        assertThat(warehouse.getProducts()).containsOnly(productBack);
        assertThat(productBack.getWarehouses()).containsOnly(warehouse);

        warehouse.removeProducts(productBack);
        assertThat(warehouse.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getWarehouses()).doesNotContain(warehouse);

        warehouse.products(new HashSet<>(Set.of(productBack)));
        assertThat(warehouse.getProducts()).containsOnly(productBack);
        assertThat(productBack.getWarehouses()).containsOnly(warehouse);

        warehouse.setProducts(new HashSet<>());
        assertThat(warehouse.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getWarehouses()).doesNotContain(warehouse);
    }
}
