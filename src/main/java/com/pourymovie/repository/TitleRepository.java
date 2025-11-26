package com.pourymovie.repository;

import com.pourymovie.entity.TitleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TitleRepository extends JpaRepository<TitleEntity, Long> {
  Optional<TitleEntity> findBySlug(String slug);

  Optional<TitleEntity> findBySlugLikeOrTitleEnLikeIgnoreCase(String slug, String titleEn);
}
