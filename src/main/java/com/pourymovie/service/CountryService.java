package com.pourymovie.service;

import com.pourymovie.dto.response.CountryDetailsDto;
import com.pourymovie.dto.response.CountryDto;
import com.pourymovie.entity.CountryEntity;
import com.pourymovie.mapper.CountryMapper;
import com.pourymovie.repository.CountryRepository;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CountryService {

  private final CountryRepository countryRepository;

  private final CountryMapper countryMapper;

  @Cacheable(value = "countries:list")
  public List<CountryDto> getAll() {
    return countryMapper.toDto(countryRepository.findAll());
  }

  @Cacheable(value = "countries:slug" , key = "#slug")
  public CountryDetailsDto getBySlug(String slug) {
    return countryMapper.toDetailsDto(countryRepository.findBySlug(slug));
  }

  @Cacheable(value = "countries:id" , key = "#id")
  public CountryEntity getById(Long id) {
    return countryRepository
        .findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }
}
