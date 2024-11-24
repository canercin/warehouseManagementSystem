package com.warehouse.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.warehouse.app.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class BatchDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BatchDTO.class);
        BatchDTO batchDTO1 = new BatchDTO();
        batchDTO1.setId(UUID.randomUUID());
        BatchDTO batchDTO2 = new BatchDTO();
        assertThat(batchDTO1).isNotEqualTo(batchDTO2);
        batchDTO2.setId(batchDTO1.getId());
        assertThat(batchDTO1).isEqualTo(batchDTO2);
        batchDTO2.setId(UUID.randomUUID());
        assertThat(batchDTO1).isNotEqualTo(batchDTO2);
        batchDTO1.setId(null);
        assertThat(batchDTO1).isNotEqualTo(batchDTO2);
    }
}
