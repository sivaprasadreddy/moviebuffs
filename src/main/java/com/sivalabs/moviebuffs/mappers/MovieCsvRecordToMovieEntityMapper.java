package com.sivalabs.moviebuffs.mappers;

import com.sivalabs.moviebuffs.model.MovieCsvRecord;
import com.sivalabs.moviebuffs.entity.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieCsvRecordToMovieEntityMapper {

    public Movie map(MovieCsvRecord movieCsvRecord) {
        Movie movie = new Movie();
        movie.setTitle(movieCsvRecord.getTitle());
        movie.setImdbId(movieCsvRecord.getImdb_id());
        movie.setBudget(movieCsvRecord.getBudget());
        movie.setHomepage(movieCsvRecord.getHomepage());
        movie.setPosterPath(movieCsvRecord.getPoster_path());
        movie.setOverview(movieCsvRecord.getOverview());
        movie.setRevenue(movieCsvRecord.getRevenue());
        movie.setRuntime(movieCsvRecord.getRuntime());
        movie.setTagline(movieCsvRecord.getTagline());
        movie.setReleaseDate(movieCsvRecord.getRelease_date());
        movie.setOriginalLanguage(movieCsvRecord.getOriginal_language());
        return movie;
    }
}
