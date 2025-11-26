package com.pourymovie.repository;

import com.pourymovie.entity.TitlePeopleEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TitlePeopleRepository extends JpaRepository<TitlePeopleEntity, Long> {
  List<TitlePeopleEntity> findAllByPersonIdIn(List<Long> ids);
}
