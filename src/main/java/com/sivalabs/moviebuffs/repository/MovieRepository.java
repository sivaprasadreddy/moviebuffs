package com.sivalabs.moviebuffs.repository;

import com.sivalabs.moviebuffs.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

}
