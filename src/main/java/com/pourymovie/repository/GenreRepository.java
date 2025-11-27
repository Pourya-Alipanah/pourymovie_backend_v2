package com.pourymovie.repository;

import com.pourymovie.entity.GenreEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, Long> {
  Optional<GenreEntity> findGenreEntityBySlug(String slug);
}
