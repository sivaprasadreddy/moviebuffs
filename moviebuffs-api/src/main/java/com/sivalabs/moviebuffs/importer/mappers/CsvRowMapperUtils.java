package com.sivalabs.moviebuffs.importer.mappers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.moviebuffs.entity.CastMember;
import com.sivalabs.moviebuffs.entity.CrewMember;
import com.sivalabs.moviebuffs.entity.Genre;
import com.sivalabs.moviebuffs.entity.Movie;
import com.sivalabs.moviebuffs.importer.model.CastMemberRecord;
import com.sivalabs.moviebuffs.importer.model.CrewMemberRecord;
import com.sivalabs.moviebuffs.importer.model.MovieCsvRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

@Component
public class CsvRowMapperUtils {
    ObjectMapper objectMapper = new ObjectMapper();

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    @Autowired
    public CsvRowMapperUtils() {
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    public Movie mapToMovieEntity(MovieCsvRecord movieCsvRecord) throws JsonProcessingException {
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
        movie.setReleaseDate(toLocalDate(movieCsvRecord.getRelease_date()));
        movie.setOriginalLanguage(movieCsvRecord.getOriginal_language());
        movie.setVoteAverage(safeDouble(movieCsvRecord.getVote_average()));
        movie.setVoteCount(safeDouble(movieCsvRecord.getVote_count()));
        movie.setPrice(randomPrice());
        movie.setGenres(convertToGenres(movieCsvRecord.getGenres()));
        return movie;
    }

    private BigDecimal randomPrice() {
        int min = 10, max = 100;
        Random r = new Random();
        return new BigDecimal(r.nextInt((max - min) + 1) + min);
    }

    private LocalDate toLocalDate(String dateString) {
        if(StringUtils.trimToNull(dateString) == null) return null;
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private List<Genre> convertToGenres(String genresString) throws JsonProcessingException {
        Genre[] genres = objectMapper.readValue(genresString, Genre[].class);
        for (Genre genre : genres) {
            genre.setSlug(toSlug(genre.getName()));
        }
        return Arrays.asList(genres);
    }

    public CrewMember mapToCrewMemberEntity(CrewMemberRecord crewMemberRecord) {
        CrewMember crewMember = new CrewMember();
        crewMember.setUid(crewMemberRecord.getId());
        crewMember.setCreditId(crewMemberRecord.getCreditId());
        crewMember.setDepartment(crewMemberRecord.getDepartment());
        crewMember.setGender(crewMemberRecord.getGender());
        crewMember.setJob(crewMemberRecord.getJob());
        crewMember.setProfilePath(crewMemberRecord.getProfilePath());
        crewMember.setName(crewMemberRecord.getName());
        return crewMember;
    }

    public CastMember mapToCastMemberEntity(CastMemberRecord castMemberRecord) {
        CastMember castMember = new CastMember();
        castMember.setUid(castMemberRecord.getId());
        castMember.setCastId(castMemberRecord.getCastId());
        castMember.setCreditId(castMemberRecord.getCreditId());
        castMember.setCharacter(castMemberRecord.getCharacter());
        castMember.setGender(castMemberRecord.getGender());
        castMember.setName(castMemberRecord.getName());
        castMember.setOrder(castMemberRecord.getOrder());
        castMember.setProfilePath(castMemberRecord.getProfilePath());
        return castMember;
    }

    public static String toSlug(String input) {
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }

    public static Double safeDouble(String str){
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            e.printStackTrace();
            return 0d;
        }
    }
}
