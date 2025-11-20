package com.pourymovie.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pourymovie.entity.CountryEntity;
import com.pourymovie.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Component
public class CountrySeeder implements CommandLineRunner {

  @Value("classpath:countries.json")
  private Resource countriesFile;

  @Autowired
  private CountryRepository countryRepository;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void run(String... args) throws Exception {
    if (countryRepository.count() == 0) {

      List<CountryEntity> countries;
      try (InputStream inputStream = countriesFile.getInputStream()) {
        CountryEntity[] arr = objectMapper.readValue(inputStream, CountryEntity[].class);
        countries = Arrays.asList(arr);
      }

      countryRepository.saveAll(countries);
    }
  }
}
