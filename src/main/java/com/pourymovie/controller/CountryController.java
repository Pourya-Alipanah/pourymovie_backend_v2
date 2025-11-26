package com.pourymovie.controller;

import com.pourymovie.dto.response.CountryDetailsDto;
import com.pourymovie.dto.response.CountryDto;
import com.pourymovie.service.CountryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("country")
@Tag(name = "country" , description = "Endpoints for managing countries")
public class CountryController {

  @Autowired
  private CountryService countryService;

  @GetMapping
  public List<CountryDto> getCountries() {
    return countryService.getAll();
  }

  @GetMapping("/{slug}")
  public CountryDetailsDto getCountry(@PathVariable String slug) {
    return countryService.getBySlug(slug);
  }
}
