package com.pourymovie.service;

import com.pourymovie.dto.request.CreateGenreDto;
import com.pourymovie.dto.request.UpdateGenreDto;
import com.pourymovie.dto.response.GenreDto;
import com.pourymovie.entity.GenreEntity;
import com.pourymovie.mapper.GenreMapper;
import com.pourymovie.repository.GenreRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class GenreService {
  @Autowired
  private GenreRepository genreRepository;

  @Autowired
  private GenreMapper genreMapper;

  public GenreDto create(CreateGenreDto createGenreDto){
    var savedEntity = genreRepository.save(
            genreMapper.toEntity(createGenreDto)
    );
    return genreMapper.toDto(savedEntity);
  }

  public GenreDto findBySlug(String slug) {
    return genreMapper.toDto(
            genreRepository.findGenreEntityBySlug(slug)
                    .orElseThrow(
                            ()-> new ResponseStatusException(HttpStatus.NOT_FOUND)
                    )
    );
  }

  public Page<GenreDto> findAll(Pageable pageable) {
    return genreMapper.toDtoPage(genreRepository.findAll(pageable));
  }

  public List<GenreEntity> findMultipleByIds(List<Long> ids){
    return genreRepository.findAllById(ids);
  }

  public GenreDto update(Long id , UpdateGenreDto updateGenreDto) {
    var genreEntity = genreRepository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
    );
    genreMapper.updateEntityFromDto(updateGenreDto, genreEntity);
    var updatedEntity = genreRepository.save(genreEntity);
    return genreMapper.toDto(updatedEntity);
  }

  public void delete(Long id) {
    boolean exist = genreRepository.existsById(id);
    if (!exist) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    genreRepository.deleteById(id);
  }
}
