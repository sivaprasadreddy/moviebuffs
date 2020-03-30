package com.sivalabs.moviebuffs.core.repository;

import com.sivalabs.moviebuffs.core.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

	@Query("select m from Movie m join m.genres g join m.castMembers cast join m.crewMembers crew")
	Page<Movie> findMoviesWithCastAndCrew(Pageable pageable);

	@Query("select distinct m from Movie m inner join m.genres g where g.id = :genreId and m.title like :query")
	Page<Movie> findByGenre(@Param("genreId") Long genreId, @Param("query") String query, Pageable pageable);

	Page<Movie> findByTitleContainingIgnoreCase(String query, Pageable pageable);

	Optional<Movie> findByTmdbId(String tmdbId);

}
