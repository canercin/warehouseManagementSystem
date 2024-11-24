package com.warehouse.app.domain;

import java.util.UUID;

public class WarehouseTestSamples {

    public static Warehouse getWarehouseSample1() {
        return new Warehouse().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).name("name1");
    }

    public static Warehouse getWarehouseSample2() {
        return new Warehouse().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).name("name2");
    }

    public static Warehouse getWarehouseRandomSampleGenerator() {
        return new Warehouse().id(UUID.randomUUID()).name(UUID.randomUUID().toString());
    }
}
