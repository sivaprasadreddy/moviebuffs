package com.sivalabs.moviebuffs.importer;

import com.sivalabs.moviebuffs.core.entity.Genre;
import com.sivalabs.moviebuffs.core.entity.Movie;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovieRowMapperUtils {

    private static final Random random = new Random();

    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");

    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public Movie mapToMovieEntity(MovieJsonRecord movieJsonRecord) {
        Movie movie = new Movie();
        movie.setTitle(movieJsonRecord.getTitle());
        movie.setTmdbId(movieJsonRecord.getId());
        movie.setImdbId(movieJsonRecord.getImdbId());
        movie.setBudget(movieJsonRecord.getBudget());
        movie.setHomepage(movieJsonRecord.getHomepage());
        movie.setPosterPath(movieJsonRecord.getPosterPath());
        movie.setOverview(movieJsonRecord.getOverview());
        movie.setRevenue(movieJsonRecord.getRevenue());
        movie.setRuntime(movieJsonRecord.getRuntime());
        movie.setTagline(movieJsonRecord.getTagline());
        movie.setReleaseDate(toLocalDate(movieJsonRecord.getReleaseDate()));
        movie.setOriginalLanguage(movieJsonRecord.getOriginalLanguage());
        movie.setVoteAverage(movieJsonRecord.getVoteAverage());
        movie.setVoteCount(movieJsonRecord.getVoteCount());
        movie.setPrice(randomPrice());
        movie.setGenres(convertToGenres(movieJsonRecord.getGenres()));
        return movie;
    }

    private BigDecimal randomPrice() {
        int min = 10, max = 100;
        return new BigDecimal(random.nextInt((max - min) + 1) + min);
    }

    private LocalDate toLocalDate(String dateString) {
        if (StringUtils.trimToNull(dateString) == null) return null;
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private Set<Genre> convertToGenres(List<MovieJsonRecord.Genre> genres) {
        Set<Genre> genreSet = new HashSet<>();
        for (MovieJsonRecord.Genre genre : genres) {
            Genre genreEntity = new Genre();
            genreEntity.setTmdbId((long) genre.getId());
            genreEntity.setSlug(toSlug(genre.getName()));
            genreEntity.setName(genre.getName());

            genreSet.add(genreEntity);
        }
        return genreSet;
    }

    public static String toSlug(String input) {
        String noWhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(noWhitespace, Normalizer.Form.NFD);
        String slug = NON_LATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }
}
