package com.pourymovie.service;

import com.pourymovie.entity.PeopleEntity;
import com.pourymovie.entity.TitlePeopleEntity;
import com.pourymovie.repository.TitlePeopleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TitlePeopleService {

  @Autowired
  private TitlePeopleRepository titlePeopleRepository;

  public List<TitlePeopleEntity> createBulk(List<TitlePeopleEntity> titlePeopleEntities){
    return titlePeopleRepository.saveAll(titlePeopleEntities);
  }

  public List<TitlePeopleEntity> findMultipleTitlePeopleByIds(List<Long> ids){
    var results = titlePeopleRepository.findAllByPersonIdIn(ids);

    Set<Long> foundIds = results.stream()
            .map(TitlePeopleEntity::getId)
            .collect(Collectors.toSet());

    List<Long> missing = ids.stream()
            .filter(id -> !foundIds.contains(id))
            .toList();

    if (!missing.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND , "Some TitlePeople IDs not found: " + missing);
    }

    return results;
  }
}
