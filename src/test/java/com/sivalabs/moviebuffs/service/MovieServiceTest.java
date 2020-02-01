package com.sivalabs.moviebuffs.service;

import com.sivalabs.moviebuffs.repository.CastMemberRepository;
import com.sivalabs.moviebuffs.repository.CrewMemberRepository;
import com.sivalabs.moviebuffs.repository.GenreRepository;
import com.sivalabs.moviebuffs.repository.MovieRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;
    @Mock
    private CastMemberRepository castMemberRepository;
    @Mock
    private CrewMemberRepository crewMemberRepository;
    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private MovieService movieService;

}
