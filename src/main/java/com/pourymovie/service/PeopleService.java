package com.pourymovie.service;

import com.pourymovie.dto.request.CreatePeopleDto;
import com.pourymovie.dto.request.UpdatePeopleDto;
import com.pourymovie.dto.response.PeopleDetailsDto;
import com.pourymovie.dto.response.PeopleDto;
import com.pourymovie.entity.PeopleEntity;
import com.pourymovie.enums.UploadFromEntity;
import com.pourymovie.enums.UploadType;
import com.pourymovie.mapper.PeopleMapper;
import com.pourymovie.repository.PeopleRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PeopleService {
  @Autowired
  private PeopleRepository peopleRepository;

  @Autowired
  private PeopleMapper peopleMapper;

  @Autowired
  private UploadCenterService uploadCenterService;

  public PeopleDetailsDto create(CreatePeopleDto createPeopleDto) throws Exception {
    var peopleEntity = peopleMapper.toEntity(createPeopleDto);

    if(createPeopleDto.imageUrl() != null){
      var imageUrl = uploadCenterService.confirmUpload(
              createPeopleDto.imageUrl().key(),
              UploadFromEntity.PERSON,
              UploadType.PROFILE
      );
      peopleEntity.setImageUrl(imageUrl);
    }

    var savedEntity = peopleRepository.save(peopleEntity);
    return peopleMapper.toDetailsDto(savedEntity);
  }

  public PeopleEntity findById(Long id) {
    return peopleRepository.findById(id).orElseThrow(
            ()-> new ResponseStatusException(HttpStatus.NOT_FOUND)
    );
  }

  public Page<PeopleDto> findAll(Pageable pageable) {
    var peopleEntities = peopleRepository.findAll(pageable);
    return peopleMapper.toDto(peopleEntities);
  }

  public PeopleDetailsDto findBySlug(String slug){
    var peopleEntity = peopleRepository.findBySlug(slug).orElseThrow(
            ()-> new ResponseStatusException(HttpStatus.NOT_FOUND)
    );
    return peopleMapper.toDetailsDto(peopleEntity);
  }

  public List<PeopleEntity> findMultipleByIds(List<Long> ids){
    var results = peopleRepository.findAllById(ids);

    Set<Long> foundIds = results.stream()
            .map(PeopleEntity::getId)
            .collect(Collectors.toSet());

    List<Long> missing = ids.stream()
            .filter(id -> !foundIds.contains(id))
            .toList();

    if (!missing.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND , "Some TitlePeople IDs not found: " + missing);
    }


    return results;
  }

  @Transactional
  public PeopleDetailsDto update(UpdatePeopleDto updatePeopleDto , Long id) throws Exception {
    var peopleEntity = findById(id);
    peopleMapper.updateEntityFromDto(updatePeopleDto, peopleEntity);

    if(updatePeopleDto.imageUrl() != null){
      var imageUrl = uploadCenterService.confirmUpload(
              updatePeopleDto.imageUrl().key(),
              UploadFromEntity.PERSON,
              UploadType.PROFILE
      );
      peopleEntity.setImageUrl(imageUrl);
    }

    var updatedEntity = peopleRepository.save(peopleEntity);
    return peopleMapper.toDetailsDto(updatedEntity);
  }

  public void deleteById(Long id){
    boolean isExist = peopleRepository.existsById(id);
    if (!isExist) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    peopleRepository.deleteById(id);
  }
}
