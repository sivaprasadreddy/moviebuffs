package com.sivalabs.moviebuffs.service;

import com.sivalabs.moviebuffs.entity.Genre;
import com.sivalabs.moviebuffs.models.ProductDTO;
import com.sivalabs.moviebuffs.repository.GenreRepository;
import com.sivalabs.moviebuffs.repository.MovieRepository;
import com.sivalabs.moviebuffs.web.mappers.MovieToProductDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CatalogService {
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieToProductDTOMapper movieToProductDTOMapper;

    @Transactional(readOnly = true)
    public Optional<ProductDTO> getProductById(Long id) {
        return movieRepository.findById(id).map(movieToProductDTOMapper::map);
    }

    public Page<ProductDTO> getProducts(Pageable pageable) {
        return movieRepository.findAll(pageable).map(movieToProductDTOMapper::map);
    }

    public Page<ProductDTO> findProductsByGenre(Long genreId, Pageable pageable) {
        return movieRepository.findByGenre(genreId, pageable).map(movieToProductDTOMapper::map);
    }

    public Page<ProductDTO> searchProducts(String query, Pageable pageable) {
        return movieRepository.findByTitleContainingIgnoreCase(query, pageable).map(movieToProductDTOMapper::map);
    }

    public List<Genre> findAllGenres(Sort sort) {
        return genreRepository.findAll(sort);
    }
    public Optional<Genre> findGenreBySlug(String slug) {
        return genreRepository.findBySlug(slug);
    }
}
