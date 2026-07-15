package com.pourymovie.service;

import com.pourymovie.dto.request.*;
import com.pourymovie.dto.response.TitleDetailsDto;
import com.pourymovie.dto.response.TitleDto;
import com.pourymovie.dto.response.TitleSummaryDto;
import com.pourymovie.entity.GenreEntity;
import com.pourymovie.entity.PeopleEntity;
import com.pourymovie.entity.TitleEntity;
import com.pourymovie.entity.TitlePeopleEntity;
import com.pourymovie.enums.UploadFromEntity;
import com.pourymovie.enums.UploadType;
import com.pourymovie.mapper.TitleMapper;
import com.pourymovie.mapper.TitlePeopleMapper;
import com.pourymovie.repository.TitleRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.pourymovie.specification.TitleSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TitleService {

  private final TitleRepository titleRepository;

  private final TitleMapper titleMapper;

  private final LanguageService languageService;

  private final CountryService countryService;

  private final GenreService genreService;

  private final PeopleService peopleService;

  private final TitlePeopleMapper titlePeopleMapper;

  private final UploadCenterService uploadCenterService;

  @Transactional
  @Caching(
      evict = {
        @CacheEvict(value = "titles:filter", allEntries = true),
        @CacheEvict(value = "titles:all", allEntries = true)
      })
  public TitleDetailsDto create(CreateTitleDto createTitleDto) throws Exception {
    var titleEntity = titleMapper.toEntity(createTitleDto);
    var language = languageService.getById(createTitleDto.languageId());
    var country = countryService.getById(createTitleDto.countryId());

    fillUploadUrl(
        titleEntity,
        createTitleDto.coverUrl(),
        createTitleDto.thumbnailUrl(),
        createTitleDto.trailerUrl());

    List<GenreEntity> genres = new ArrayList<>();
    List<TitlePeopleEntity> titlePeopleEntities = new ArrayList<>();

    if (!createTitleDto.genreIds().isEmpty()) {
      genres.addAll(genreService.findMultipleByIds(createTitleDto.genreIds().stream().toList()));
    }

    if (!createTitleDto.titlePeople().isEmpty()) {

      List<PeopleEntity> existingPeople =
          peopleService.findMultipleByIds(
              createTitleDto.titlePeople().stream().map(CreateTitlePeopleDto::id).toList());

      var peopleMap =
          existingPeople.stream().collect(Collectors.toMap(PeopleEntity::getId, p -> p));

      titlePeopleEntities =
          createTitleDto.titlePeople().stream()
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

  @Cacheable(value = "titles:slug", key = "#slug")
  @Transactional(readOnly = true)
  public TitleDetailsDto findBySlug(String slug) {
    return titleMapper.toDetailsDto(titleRepository.findBySlug(slug).orElseThrow());
  }

  @Cacheable(value = "titles:title", key = "#title")
  @Transactional(readOnly = true)
  public TitleSummaryDto findLinkByTitleName(String title) {
    var titleEntT =
        titleRepository
            .findBySlugLikeOrTitleEnLikeIgnoreCase(title, title)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    return titleMapper.toSummaryDto(titleEntT);
  }

  @Transactional(readOnly = true)
  @Cacheable(value = "titles:id", key = "#id")
  public TitleEntity findById(Long id) {
    return titleRepository
        .findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  @Transactional(readOnly = true)
  @Cacheable(
      value = "titles:all",
      key = "#pageable.pageNumber + '_' + #pageable.pageSize+ '_' + #pageable.sort.toString()")
  public Page<TitleDto> findAll(Pageable pageable) {
    return titleMapper.toDtoPage(titleRepository.findAll(pageable));
  }

  @Transactional(readOnly = true)
  @Cacheable(
      value = "titles:filter",
      key =
          "#filters.toString() + '_' + #pageable.pageNumber + '_' + #pageable.pageSize+ '_' + #pageable.sort.toString()")
  public Page<TitleDto> findAll(TitleFilterDto filters, Pageable pageable) {
    Specification<TitleEntity> spec = TitleSpecification.withFilters(filters);
    return titleMapper.toDtoPage(titleRepository.findAll(spec, pageable));
  }

  @Transactional
  @Caching(
      evict = {
        @CacheEvict(value = "titles:id", key = "#id"),
        @CacheEvict(value = "titles:title", allEntries = true),
        @CacheEvict(value = "titles:filter", allEntries = true),
        @CacheEvict(value = "titles:all", allEntries = true),
        @CacheEvict(value = "titles:slug", allEntries = true),
      })
  public TitleDetailsDto update(UpdateTitleDto updateTitleDto, Long id) throws Exception {
    var titleEntity =
        titleRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    titleMapper.updateEntityFromDto(updateTitleDto, titleEntity);

    fillUploadUrl(
        titleEntity,
        updateTitleDto.coverUrl(),
        updateTitleDto.thumbnailUrl(),
        updateTitleDto.trailerUrl());

    if (updateTitleDto.languageId() != null) {
      titleEntity.setLanguage(languageService.getById(updateTitleDto.languageId()));
    }

    if (updateTitleDto.countryId() != null) {
      titleEntity.setCountry(countryService.getById(updateTitleDto.countryId()));
    }

    if (updateTitleDto.genreIds() != null) {
      titleEntity.setGenres(
          genreService.findMultipleByIds(new ArrayList<>(updateTitleDto.genreIds())));
    }

    if (updateTitleDto.titlePeople() != null) {
      if (updateTitleDto.titlePeople().isEmpty()) {
        titleEntity.getPeople().clear();
      } else {
        List<PeopleEntity> existingPeople =
            peopleService.findMultipleByIds(
                updateTitleDto.titlePeople().stream().map(CreateTitlePeopleDto::id).toList());

        var peopleMap =
            existingPeople.stream().collect(Collectors.toMap(PeopleEntity::getId, p -> p));

        var people =
            updateTitleDto.titlePeople().stream()
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

  private void fillUploadUrl(
      TitleEntity titleEntity,
      ConfirmUploadDto coverUploadDto,
      ConfirmUploadDto thumbnailUrlDto,
      ConfirmUploadDto trailerUrlDto)
      throws Exception {
    if (coverUploadDto != null) {
      var coverUploadUrl =
          uploadCenterService.confirmUpload(
              coverUploadDto.key(), UploadFromEntity.TITLE, UploadType.COVER);
      titleEntity.setCoverUrl(coverUploadUrl);
    }

    if (thumbnailUrlDto != null) {
      var thumbnailUrl =
          uploadCenterService.confirmUpload(
              thumbnailUrlDto.key(), UploadFromEntity.TITLE, UploadType.THUMBNAIL);
      titleEntity.setThumbnailUrl(thumbnailUrl);
    }

    if (trailerUrlDto != null) {
      var trailerUrl =
          uploadCenterService.confirmUpload(
              trailerUrlDto.key(), UploadFromEntity.TITLE, UploadType.TRAILER);
      titleEntity.setTrailerUrl(trailerUrl);
    }
  }

  @Caching(
      evict = {
        @CacheEvict(value = "titles:id", key = "#id"),
        @CacheEvict(value = "titles:title", allEntries = true),
        @CacheEvict(value = "titles:filter", allEntries = true),
        @CacheEvict(value = "titles:all", allEntries = true),
        @CacheEvict(value = "titles:slug", allEntries = true),
      })
  @Transactional
  public void deleteById(Long id) {
    titleRepository.deleteById(id);
  }
}
