package com.pourymovie.service;

import com.pourymovie.dto.request.CreateTitleDto;
import com.pourymovie.dto.request.CreateTitlePeopleDto;
import com.pourymovie.dto.request.UpdateTitleDto;
import com.pourymovie.dto.response.TitleDetailsDto;
import com.pourymovie.dto.response.TitleDto;
import com.pourymovie.entity.GenreEntity;
import com.pourymovie.entity.PeopleEntity;
import com.pourymovie.entity.TitleEntity;
import com.pourymovie.entity.TitlePeopleEntity;
import com.pourymovie.mapper.TitleMapper;
import com.pourymovie.mapper.TitlePeopleMapper;
import com.pourymovie.repository.TitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TitleService {

  @Autowired
  private TitleRepository titleRepository;

  @Autowired
  private TitleMapper titleMapper;

  @Autowired
  private LanguageService languageService;

  @Autowired
  private CountryService countryService;

  @Autowired
  private GenreService genreService;

  @Autowired
  private PeopleService peopleService;

  @Autowired
  private TitlePeopleMapper titlePeopleMapper;

  @Transactional
  public TitleDetailsDto create(CreateTitleDto createTitleDto) {
    var titleEntity = titleMapper.toEntity(createTitleDto);
    var language = languageService.getById(createTitleDto.languageId());
    var country = countryService.getById(createTitleDto.countryId());

    List<GenreEntity> genres = new ArrayList<>();
    List<TitlePeopleEntity> titlePeopleEntities = new ArrayList<>();

    if (!createTitleDto.genreIds().isEmpty()) {
      genres.addAll(genreService.findMultipleByIds(createTitleDto.genreIds().stream().toList()));
    }

    if (!createTitleDto.titlePeople().isEmpty()) {

      List<PeopleEntity> existingPeople = peopleService.findMultipleByIds(
              createTitleDto.titlePeople().stream().map(CreateTitlePeopleDto::id).toList()
      );

      var peopleMap = existingPeople.stream()
              .collect(Collectors.toMap(PeopleEntity::getId, p -> p));

      titlePeopleEntities = createTitleDto.titlePeople().stream()
              .map(p -> titlePeopleMapper.toEntity(p, peopleMap.get(p.id())))
              .peek(tpe -> tpe.setTitle(titleEntity))
              .toList();
    }

    titleEntity.setLanguage(language);
    titleEntity.setCountry(country);
    titleEntity.setGenres(genres);
    titleEntity.setPeople(titlePeopleEntities);
    var savedEntity = titleRepository.save(titleEntity);
    return titleMapper.toDetailsDto(savedEntity);
  }

  public TitleDetailsDto findBySlug(String slug) {
    return titleMapper.toDetailsDto(titleRepository.findBySlug(slug).orElseThrow());
  }

  public TitleEntity findById(Long id) {
    return titleRepository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
    );
  }

  public Page<TitleDto> findAll(Pageable pageable) {
    return titleMapper.toDtoPage(titleRepository.findAll(pageable));
  }

  @Transactional
  public TitleDetailsDto update(UpdateTitleDto updateTitleDto, Long id) {
    var titleEntity = findById(id);
    titleMapper.updateEntityFromDto(updateTitleDto, titleEntity);
    if (updateTitleDto.languageId() != null) {
      titleEntity.setLanguage(languageService.getById(updateTitleDto.languageId()));
    }

    if (updateTitleDto.countryId() != null) {
      titleEntity.setCountry(countryService.getById(updateTitleDto.countryId()));
    }

    if (updateTitleDto.genreIds() != null) {
      titleEntity.setGenres(genreService.findMultipleByIds(new ArrayList<>(updateTitleDto.genreIds())));
    }

    if (updateTitleDto.titlePeople() != null) {
      if (updateTitleDto.titlePeople().isEmpty()) {
        titleEntity.getPeople().clear();
      } else {
        List<PeopleEntity> existingPeople = peopleService.findMultipleByIds(
                updateTitleDto.titlePeople().stream().map(CreateTitlePeopleDto::id).toList()
        );

        var peopleMap = existingPeople.stream()
                .collect(Collectors.toMap(PeopleEntity::getId, p -> p));

        var people = updateTitleDto.titlePeople().stream()
                .map(p -> titlePeopleMapper.toEntity(p, peopleMap.get(p.id())))
                .peek(tpe -> tpe.setTitle(titleEntity))
                .collect(Collectors.toCollection(ArrayList::new));

        titleEntity.getPeople().clear();
        titleEntity.getPeople().addAll(people);
      }
    }

    var updatedEntity = titleRepository.save(titleEntity);
    return titleMapper.toDetailsDto(updatedEntity);
  }

  public void deleteById(Long id) {
    titleRepository.deleteById(id);
  }
}
