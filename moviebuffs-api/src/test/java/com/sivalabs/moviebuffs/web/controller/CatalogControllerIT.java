package com.sivalabs.moviebuffs.web.controller;

import com.sivalabs.moviebuffs.common.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

import static com.sivalabs.moviebuffs.utils.TestConstants.PRODUCTS_COLLECTION_BASE_PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CatalogControllerIT extends AbstractIntegrationTest {

    @Test
    void shouldFetchAllProducts() throws Exception {
        this.mockMvc.perform(get(PRODUCTS_COLLECTION_BASE_PATH))
                .andExpect(status().isOk());
    }

}
