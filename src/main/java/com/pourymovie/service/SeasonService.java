package com.pourymovie.service;

import com.pourymovie.dto.request.CreateSeasonDto;
import com.pourymovie.dto.response.SeasonDto;
import com.pourymovie.entity.SeasonEntity;
import com.pourymovie.mapper.SeasonMapper;
import com.pourymovie.repository.SeasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SeasonService {
  @Autowired
  SeasonRepository seasonRepository;

  @Autowired
  SeasonMapper seasonMapper;

  public List<SeasonDto> getAllSeasonsByTitleId(Long titleId) {
    return seasonMapper.toDto(seasonRepository.getSeasonByTitleId(titleId));
  }

  public void deleteSeasonById(Long id) {
    boolean isExist = seasonRepository.existsById(id);
    if (!isExist) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    seasonRepository.deleteById(id);
  }

  public SeasonDto getSeasonById(Long id) {
    return seasonMapper.toDto(seasonRepository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
    ));
  }

  public SeasonDto createSeason(CreateSeasonDto seasonDto) {
    SeasonEntity seasonEntity = seasonMapper.toEntity(seasonDto);
    seasonEntity.setTitle(seasonEntity.getTitle());
    var savedEntity = seasonRepository.save(seasonEntity);
    return seasonMapper.toDto(savedEntity);
  }
}
