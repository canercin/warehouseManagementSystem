package com.warehouse.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.warehouse.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SupplierContactDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierContactDTO.class);
        SupplierContactDTO supplierContactDTO1 = new SupplierContactDTO();
        supplierContactDTO1.setId(1L);
        SupplierContactDTO supplierContactDTO2 = new SupplierContactDTO();
        assertThat(supplierContactDTO1).isNotEqualTo(supplierContactDTO2);
        supplierContactDTO2.setId(supplierContactDTO1.getId());
        assertThat(supplierContactDTO1).isEqualTo(supplierContactDTO2);
        supplierContactDTO2.setId(2L);
        assertThat(supplierContactDTO1).isNotEqualTo(supplierContactDTO2);
        supplierContactDTO1.setId(null);
        assertThat(supplierContactDTO1).isNotEqualTo(supplierContactDTO2);
    }
}
