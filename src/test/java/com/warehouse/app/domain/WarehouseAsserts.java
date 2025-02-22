package com.warehouse.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class WarehouseAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertWarehouseAllPropertiesEquals(Warehouse expected, Warehouse actual) {
        assertWarehouseAutoGeneratedPropertiesEquals(expected, actual);
        assertWarehouseAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertWarehouseAllUpdatablePropertiesEquals(Warehouse expected, Warehouse actual) {
        assertWarehouseUpdatableFieldsEquals(expected, actual);
        assertWarehouseUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertWarehouseAutoGeneratedPropertiesEquals(Warehouse expected, Warehouse actual) {
        assertThat(expected)
            .as("Verify Warehouse auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertWarehouseUpdatableFieldsEquals(Warehouse expected, Warehouse actual) {
        assertThat(expected)
            .as("Verify Warehouse relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertWarehouseUpdatableRelationshipsEquals(Warehouse expected, Warehouse actual) {
        assertThat(expected)
            .as("Verify Warehouse relationships")
            .satisfies(e -> assertThat(e.getProducts()).as("check products").isEqualTo(actual.getProducts()));
    }
}
