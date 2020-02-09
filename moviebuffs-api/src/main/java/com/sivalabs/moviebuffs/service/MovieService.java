package com.sivalabs.moviebuffs.service;

import com.sivalabs.moviebuffs.entity.CastMember;
import com.sivalabs.moviebuffs.entity.CrewMember;
import com.sivalabs.moviebuffs.entity.Genre;
import com.sivalabs.moviebuffs.entity.Movie;
import com.sivalabs.moviebuffs.models.ProductDTO;
import com.sivalabs.moviebuffs.repository.CastMemberRepository;
import com.sivalabs.moviebuffs.repository.CrewMemberRepository;
import com.sivalabs.moviebuffs.repository.GenreRepository;
import com.sivalabs.moviebuffs.repository.MovieRepository;
import com.sivalabs.moviebuffs.web.mappers.MovieToProductDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final CastMemberRepository castMemberRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final GenreRepository genreRepository;
    private final MovieToProductDTOMapper movieToProductDTOMapper;

    @Transactional(readOnly = true)
    public Optional<Movie> findMovieById(Long id) {
        return movieRepository.findById(id);
    }

    public Movie createMovie(Movie movie) {
        List<Genre> genreList = saveGenres(movie.getGenres());
        movie.setGenres(genreList);
        return movieRepository.save(movie);
    }

    private List<Genre> saveGenres(List<Genre> genres) {
        List<Genre> genreList = new ArrayList<>();
        for (Genre genre : genres) {
            Optional<Genre> byId = genreRepository.findByName(genre.getName());
            if(byId.isPresent()) {
                genreList.add(byId.get());
            } else {
                genreList.add(genreRepository.save(genre));
            }
        }
        return genreList;
    }

    public Page<ProductDTO> getMovies(Pageable pageable) {
        return movieRepository.findAll(pageable).map(movieToProductDTOMapper::map);
    }

    public CastMember save(CastMember castMember) {
        return castMemberRepository.save(castMember);
    }

    public CrewMember save(CrewMember crewMember) {
        return crewMemberRepository.save(crewMember);
    }

}
