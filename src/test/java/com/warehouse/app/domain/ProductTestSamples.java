package com.warehouse.app.domain;

import java.util.UUID;

public class ProductTestSamples {

    public static Product getProductSample1() {
        return new Product().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).name("name1").description("description1");
    }

    public static Product getProductSample2() {
        return new Product().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).name("name2").description("description2");
    }

    public static Product getProductRandomSampleGenerator() {
        return new Product().id(UUID.randomUUID()).name(UUID.randomUUID().toString()).description(UUID.randomUUID().toString());
    }
}
