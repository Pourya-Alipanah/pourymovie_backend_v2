package com.pourymovie.repository;

import com.pourymovie.entity.VideoLinkEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoLinkRepository extends JpaRepository<VideoLinkEntity,Long> {
  List<VideoLinkEntity> findAllByEpisodeId(Long episodeId);

  List<VideoLinkEntity> findAllByTitleId(Long titleID);
}
