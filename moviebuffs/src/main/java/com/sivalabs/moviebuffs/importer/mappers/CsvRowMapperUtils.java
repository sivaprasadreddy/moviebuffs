package com.sivalabs.moviebuffs.importer.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.moviebuffs.core.entity.CastMember;
import com.sivalabs.moviebuffs.core.entity.CrewMember;
import com.sivalabs.moviebuffs.core.entity.Genre;
import com.sivalabs.moviebuffs.core.entity.Movie;
import com.sivalabs.moviebuffs.importer.model.CastMemberRecord;
import com.sivalabs.moviebuffs.importer.model.CrewMemberRecord;
import com.sivalabs.moviebuffs.importer.model.MovieCsvRecord;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class CsvRowMapperUtils {

	private final ObjectMapper objectMapper;

	private static final Random random = new Random();

	private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");

	private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

	public Movie mapToMovieEntity(MovieCsvRecord movieCsvRecord) throws JsonProcessingException {
		Movie movie = new Movie();
		movie.setTitle(movieCsvRecord.getTitle());
		movie.setTmdbId(movieCsvRecord.getId());
		movie.setImdbId(movieCsvRecord.getImdbId());
		movie.setBudget(movieCsvRecord.getBudget());
		movie.setHomepage(movieCsvRecord.getHomepage());
		movie.setPosterPath(movieCsvRecord.getPosterPath());
		movie.setOverview(movieCsvRecord.getOverview());
		movie.setRevenue(movieCsvRecord.getRevenue());
		movie.setRuntime(movieCsvRecord.getRuntime());
		movie.setTagline(movieCsvRecord.getTagline());
		movie.setReleaseDate(toLocalDate(movieCsvRecord.getReleaseDate()));
		movie.setOriginalLanguage(movieCsvRecord.getOriginalLanguage());
		movie.setVoteAverage(safeDouble(movieCsvRecord.getVoteAverage()));
		movie.setVoteCount(safeDouble(movieCsvRecord.getVoteCount()));
		movie.setPrice(randomPrice());
		movie.setGenres(convertToGenres(movieCsvRecord.getGenres()));
		return movie;
	}

	private BigDecimal randomPrice() {
		int min = 10, max = 100;
		return new BigDecimal(random.nextInt((max - min) + 1) + min);
	}

	private LocalDate toLocalDate(String dateString) {
		if (StringUtils.trimToNull(dateString) == null)
			return null;
		return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	private Set<Genre> convertToGenres(String genresString) throws JsonProcessingException {
		Genre[] genres = objectMapper.readValue(genresString, Genre[].class);
		for (Genre genre : genres) {
			genre.setSlug(toSlug(genre.getName()));
		}
		return new HashSet<>(Arrays.asList(genres));
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
		String noWhitespace = WHITESPACE.matcher(input).replaceAll("-");
		String normalized = Normalizer.normalize(noWhitespace, Normalizer.Form.NFD);
		String slug = NON_LATIN.matcher(normalized).replaceAll("");
		return slug.toLowerCase(Locale.ENGLISH);
	}

	public static Double safeDouble(String str) {
		try {
			return Double.parseDouble(str);
		}
		catch (Exception e) {
			return 0d;
		}
	}

}
