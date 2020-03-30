package com.sivalabs.moviebuffs.importer;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "application.dataimport")
@Getter
@Setter
public class DataImportProperties {

	private Tmdb tmdb = new Tmdb();

	@Getter
	@Setter
	public static class Tmdb {

		private boolean disabled;

		private int maxSize;

		private int batchSize;

		private boolean async;

		private List<String> moviesDataFiles;

		private List<String> movieCreditsFiles;

	}

}
