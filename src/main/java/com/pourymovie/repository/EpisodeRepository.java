package com.pourymovie.repository;

import com.pourymovie.entity.EpisodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EpisodeRepository extends JpaRepository<EpisodeEntity,Long> {
  List<EpisodeEntity> findAllBySeasonIdOrderByEpisodeNumberAsc(Long seasonId);
}
