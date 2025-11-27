package com.pourymovie.repository;

import com.pourymovie.entity.LanguageEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<LanguageEntity, Long> {
  Optional<LanguageEntity> findBySlug(String slug);
}
