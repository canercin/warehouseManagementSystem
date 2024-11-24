package com.warehouse.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class BatchTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Batch getBatchSample1() {
        return new Batch().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).batchNumber(1).purchaseQuantity(1);
    }

    public static Batch getBatchSample2() {
        return new Batch().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).batchNumber(2).purchaseQuantity(2);
    }

    public static Batch getBatchRandomSampleGenerator() {
        return new Batch().id(UUID.randomUUID()).batchNumber(intCount.incrementAndGet()).purchaseQuantity(intCount.incrementAndGet());
    }
}
