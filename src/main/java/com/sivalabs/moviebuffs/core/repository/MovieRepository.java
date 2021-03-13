package com.sivalabs.moviebuffs.core.repository;

import com.sivalabs.moviebuffs.core.entity.Movie;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("select distinct m from Movie m inner join m.genres g where g.id = :genreId")
    Page<Movie> findByGenre(@Param("genreId") Long genreId, Pageable pageable);

    Page<Movie> findByTitleContainingIgnoreCase(String query, Pageable pageable);

    Optional<Movie> findByTmdbId(Long tmdbId);
}
