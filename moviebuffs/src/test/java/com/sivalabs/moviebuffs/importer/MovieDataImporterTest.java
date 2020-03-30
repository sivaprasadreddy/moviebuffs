package com.sivalabs.moviebuffs.importer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.moviebuffs.core.entity.Genre;
import com.sivalabs.moviebuffs.core.service.MovieService;
import com.sivalabs.moviebuffs.importer.mappers.CsvRowMapperUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class MovieDataImporterTest {

	private CsvRowMapperUtils csvRowMapperUtils;

	private DataImportProperties dataImportProperties;

	private MovieService movieService;

	private MovieDataImporter movieDataImporter;

	@BeforeEach
	void setUp() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		csvRowMapperUtils = new CsvRowMapperUtils(objectMapper);
		dataImportProperties = new DataImportProperties();
		dataImportProperties.getTmdb().setDisabled(false);
		dataImportProperties.getTmdb().setMoviesDataFiles(singletonList("/data/movies_metadata-test.csv"));
		dataImportProperties.getTmdb().setMovieCreditsFiles(singletonList("/data/credits-test.csv"));

		movieService = mock(MovieService.class);

		given(movieService.saveGenre(any(Genre.class))).willAnswer(answer -> answer.getArgument(0));

		movieDataImporter = new MovieDataImporter(movieService, csvRowMapperUtils, dataImportProperties, objectMapper);
	}

	@Test
	void should_import_movie_data_successfully() throws IOException, CsvValidationException {
		movieDataImporter.importData();
		assertTrue(true);
	}

}
