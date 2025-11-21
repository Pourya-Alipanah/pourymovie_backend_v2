package com.pourymovie.repository;

import com.pourymovie.entity.TitlePeopleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TitlePeopleRepository extends JpaRepository<TitlePeopleEntity,Long> {
  List<TitlePeopleEntity> findAllByPersonIdIn(List<Long> ids);
}
