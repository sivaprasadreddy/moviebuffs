package com.sivalabs.moviebuffs.core.repository;

import com.sivalabs.moviebuffs.core.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {

	Optional<Genre> findByName(String genre);

	Optional<Genre> findBySlug(String slug);

}
