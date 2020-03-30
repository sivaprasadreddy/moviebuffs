package com.sivalabs.moviebuffs.web.api;

import com.sivalabs.moviebuffs.common.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

import static com.sivalabs.moviebuffs.common.TestConstants.MOVIES_COLLECTION_BASE_PATH;
import static com.sivalabs.moviebuffs.common.TestConstants.MOVIES_SINGLE_BASE_PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MoviesRestControllerIT extends AbstractIntegrationTest {

	@Test
	void should_fetch_all_movies() throws Exception {
		this.mockMvc.perform(get(MOVIES_COLLECTION_BASE_PATH)).andExpect(status().isOk());
	}

	@Test
	void should_search_movies_by_title() throws Exception {
		this.mockMvc.perform(get(MOVIES_COLLECTION_BASE_PATH + "?query=hero")).andExpect(status().isOk());
	}

	@Test
	void should_fetch_movies_by_genre() throws Exception {
		this.mockMvc.perform(get(MOVIES_COLLECTION_BASE_PATH + "?genre=comedy")).andExpect(status().isOk());
	}

	@Test
	void should_fetch_movie_by_id() throws Exception {
		this.mockMvc.perform(get(MOVIES_SINGLE_BASE_PATH, 1)).andExpect(status().isOk());
	}

	@Test
	void should_return_404_when_movie_id_is_not_exists() throws Exception {
		this.mockMvc.perform(get(MOVIES_SINGLE_BASE_PATH, 999999)).andExpect(status().isNotFound());
	}

	@Test
	void should_fetch_all_genres() throws Exception {
		this.mockMvc.perform(get("/api/genres")).andExpect(status().isOk());
	}

}
