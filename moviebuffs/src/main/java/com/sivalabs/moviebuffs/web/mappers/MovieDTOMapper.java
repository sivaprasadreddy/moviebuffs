package com.sivalabs.moviebuffs.web.mappers;

import com.sivalabs.moviebuffs.core.entity.CastMember;
import com.sivalabs.moviebuffs.core.entity.CrewMember;
import com.sivalabs.moviebuffs.core.entity.Movie;
import com.sivalabs.moviebuffs.web.dto.CastMemberDTO;
import com.sivalabs.moviebuffs.web.dto.CrewMemberDTO;
import com.sivalabs.moviebuffs.web.dto.MovieDTO;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

import static com.sivalabs.moviebuffs.core.utils.Constants.TMDB_IMAGE_PATH_PREFIX;

@Component
public class MovieDTOMapper {

	public MovieDTO map(Movie movie) {
		if (movie == null)
			return null;
		return MovieDTO.builder().id(movie.getId()).budget(movie.getBudget()).homepage(movie.getHomepage())
				.imdbId(movie.getImdbId()).originalLanguage(movie.getOriginalLanguage()).overview(movie.getOverview())
				.posterPath(TMDB_IMAGE_PATH_PREFIX + movie.getPosterPath()).releaseDate(movie.getReleaseDate())
				.revenue(movie.getRevenue()).tagline(movie.getTagline()).runtime(movie.getRuntime())
				.title(movie.getTitle()).price(movie.getPrice()).genres(movie.getGenres())
				.castMembers(mapCastMembers(movie.getCastMembers())).crewMembers(mapCrewMembers(movie.getCrewMembers()))
				.build();
	}

	Set<CastMemberDTO> mapCastMembers(Set<CastMember> castMembers) {
		return castMembers.stream().map(this::mapCastMember).collect(Collectors.toSet());
	}

	CastMemberDTO mapCastMember(CastMember castMember) {
		return CastMemberDTO.builder().id(castMember.getId()).character(castMember.getCharacter())
				.name(castMember.getName()).gender(toGender(castMember.getGender())).order(castMember.getOrder())
				.profilePath(castMember.getProfilePath()).build();

	}

	Set<CrewMemberDTO> mapCrewMembers(Set<CrewMember> crewMembers) {
		return crewMembers.stream().map(this::mapCrewMember).collect(Collectors.toSet());
	}

	CrewMemberDTO mapCrewMember(CrewMember crewMember) {
		return CrewMemberDTO.builder().id(crewMember.getId()).name(crewMember.getName()).job(crewMember.getJob())
				.department(crewMember.getDepartment()).gender(toGender(crewMember.getGender()))
				.profilePath(crewMember.getProfilePath()).build();
	}

	private String toGender(String code) {
		if ("2".equals(code))
			return "Male";
		if ("1".equals(code))
			return "Feale";
		return "";
	}

}
