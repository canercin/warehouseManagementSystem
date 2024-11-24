package com.warehouse.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.warehouse.app.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class WarehouseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WarehouseDTO.class);
        WarehouseDTO warehouseDTO1 = new WarehouseDTO();
        warehouseDTO1.setId(UUID.randomUUID());
        WarehouseDTO warehouseDTO2 = new WarehouseDTO();
        assertThat(warehouseDTO1).isNotEqualTo(warehouseDTO2);
        warehouseDTO2.setId(warehouseDTO1.getId());
        assertThat(warehouseDTO1).isEqualTo(warehouseDTO2);
        warehouseDTO2.setId(UUID.randomUUID());
        assertThat(warehouseDTO1).isNotEqualTo(warehouseDTO2);
        warehouseDTO1.setId(null);
        assertThat(warehouseDTO1).isNotEqualTo(warehouseDTO2);
    }
}
