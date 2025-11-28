package com.pourymovie.repository;

import com.pourymovie.entity.TitleEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TitleRepository
    extends JpaRepository<TitleEntity, Long>, JpaSpecificationExecutor<TitleEntity> {
  Optional<TitleEntity> findBySlug(String slug);

  Optional<TitleEntity> findBySlugLikeOrTitleEnLikeIgnoreCase(String slug, String titleEn);
}
