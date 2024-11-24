package com.warehouse.app.domain;

import java.util.UUID;

public class SupplierTestSamples {

    public static Supplier getSupplierSample1() {
        return new Supplier()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .name("name1")
            .contactInfo("contactInfo1")
            .address("address1");
    }

    public static Supplier getSupplierSample2() {
        return new Supplier()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .name("name2")
            .contactInfo("contactInfo2")
            .address("address2");
    }

    public static Supplier getSupplierRandomSampleGenerator() {
        return new Supplier()
            .id(UUID.randomUUID())
            .name(UUID.randomUUID().toString())
            .contactInfo(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString());
    }
}
