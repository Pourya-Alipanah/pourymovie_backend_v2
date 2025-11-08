package com.pourymovie.repository;

import com.pourymovie.entity.SeasonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeasonRepository extends JpaRepository<SeasonEntity,Long> {
  List<SeasonEntity> getSeasonByTitleId(Long titleId);
}
