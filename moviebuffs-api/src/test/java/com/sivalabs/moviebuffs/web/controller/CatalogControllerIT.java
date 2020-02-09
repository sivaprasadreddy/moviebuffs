package com.sivalabs.moviebuffs.web.controller;

import com.sivalabs.moviebuffs.common.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CatalogControllerIT extends AbstractIntegrationTest {

    @Test
    void shouldFetchAllProducts() throws Exception {
        this.mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk());
    }

}
