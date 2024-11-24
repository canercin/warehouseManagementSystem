package com.warehouse.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SupplierContactTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SupplierContact getSupplierContactSample1() {
        return new SupplierContact().id(1L).name("name1").email("email1").phone("phone1");
    }

    public static SupplierContact getSupplierContactSample2() {
        return new SupplierContact().id(2L).name("name2").email("email2").phone("phone2");
    }

    public static SupplierContact getSupplierContactRandomSampleGenerator() {
        return new SupplierContact()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString());
    }
}
