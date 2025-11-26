package com.pourymovie.repository;

import com.pourymovie.entity.EpisodeEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodeRepository extends JpaRepository<EpisodeEntity,Long> {
  List<EpisodeEntity> findAllBySeasonIdOrderByEpisodeNumberAsc(Long seasonId);
}
