package com.pourymovie.service;

import com.pourymovie.dto.request.CreateLanguageDto;
import com.pourymovie.dto.request.UpdateLanguageDto;
import com.pourymovie.dto.response.LanguageDto;
import com.pourymovie.entity.LanguageEntity;
import com.pourymovie.mapper.LanguageMapper;
import com.pourymovie.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LanguageService {
  @Autowired
  private LanguageRepository languageRepository;

  @Autowired
  private LanguageMapper languageMapper;

  public LanguageDto create(CreateLanguageDto createLanguageDto) {
    LanguageEntity languageEntity = languageMapper.toEntity(createLanguageDto);
    return languageMapper.toDto(languageRepository.save(languageEntity));
  }

  public Page<LanguageDto> getAll(Pageable pageable) {
    return languageMapper.toDto(
            languageRepository.findAll(pageable)
    );
  }

  public LanguageDto getBySlug(String slug) {
    LanguageEntity languageEntity = languageRepository.findBySlug(slug).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
    );
    return languageMapper.toDto(languageEntity);
  }

  public LanguageDto getById(Long id) {
    LanguageEntity languageEntity = languageRepository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
    );
    return languageMapper.toDto(languageEntity);
  }

  public LanguageDto update(Long id ,UpdateLanguageDto updateLanguageDto) {
    var languageEntity = languageRepository.findById(id).orElseThrow(
            ()-> new ResponseStatusException(HttpStatus.NOT_FOUND)
    );

    languageMapper.updateEntityFromDto(updateLanguageDto, languageEntity);

    var updatedEntity = languageRepository.save(languageEntity);

    return languageMapper.toDto(updatedEntity);
  }

  public void deleteById(Long id) {
    boolean exist = languageRepository.existsById(id);
    if (!exist) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    languageRepository.deleteById(id);
  }
}
