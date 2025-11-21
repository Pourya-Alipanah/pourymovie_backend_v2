package com.pourymovie.service;

import com.pourymovie.dto.response.CountryDetailsDto;
import com.pourymovie.dto.response.CountryDto;
import com.pourymovie.entity.CountryEntity;
import com.pourymovie.mapper.CountryMapper;
import com.pourymovie.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CountryService {

  @Autowired
  private CountryRepository countryRepository;

  @Autowired
  private CountryMapper countryMapper;

  public List<CountryDto> getAll() {
    return countryMapper.toDto(countryRepository.findAll());
  }

  public CountryDetailsDto getBySlug(String slug){
    return countryMapper.toDetailsDto(countryRepository.findBySlug(slug));
  }

  public CountryEntity getById(Long id) {
    return countryRepository.findById(id).orElseThrow(
            ()-> new ResponseStatusException(HttpStatus.NOT_FOUND)
    );
  }
}
