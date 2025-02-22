package com.warehouse.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.warehouse.app.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class SupplierDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierDTO.class);
        SupplierDTO supplierDTO1 = new SupplierDTO();
        supplierDTO1.setId(UUID.randomUUID());
        SupplierDTO supplierDTO2 = new SupplierDTO();
        assertThat(supplierDTO1).isNotEqualTo(supplierDTO2);
        supplierDTO2.setId(supplierDTO1.getId());
        assertThat(supplierDTO1).isEqualTo(supplierDTO2);
        supplierDTO2.setId(UUID.randomUUID());
        assertThat(supplierDTO1).isNotEqualTo(supplierDTO2);
        supplierDTO1.setId(null);
        assertThat(supplierDTO1).isNotEqualTo(supplierDTO2);
    }
}
