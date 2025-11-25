package com.pourymovie.repository;

import com.pourymovie.entity.VideoLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoLinkRepository extends JpaRepository<VideoLinkEntity,Long> {
  List<VideoLinkEntity> findAllByEpisodeId(Long episodeId);

  List<VideoLinkEntity> findAllByTitleId(Long titleID);
}
