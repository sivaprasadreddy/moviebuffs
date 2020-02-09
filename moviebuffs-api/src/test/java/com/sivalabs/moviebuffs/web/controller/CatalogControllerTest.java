package com.sivalabs.moviebuffs.web.controller;

import com.sivalabs.moviebuffs.common.AbstractMvcUnitTest;
import com.sivalabs.moviebuffs.models.ProductDTO;
import com.sivalabs.moviebuffs.service.CatalogService;
import com.sivalabs.moviebuffs.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CatalogController.class)
public class CatalogControllerTest extends AbstractMvcUnitTest {

    @MockBean
    private CatalogService catalogService;

    @Test
    void shouldShowHomePage() throws Exception {
        Page<ProductDTO> page = new PageImpl<>(new ArrayList<>());
        given(catalogService.getProducts(any(Pageable.class))).willReturn(page);

        this.mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk());
    }
}
