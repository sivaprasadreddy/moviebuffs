package com.sivalabs.moviebuffs.core.repository;

import com.sivalabs.moviebuffs.core.entity.Genre;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    Optional<Genre> findByName(String genre);

    Optional<Genre> findBySlug(String slug);
}
