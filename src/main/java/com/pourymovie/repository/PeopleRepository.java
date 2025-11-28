package com.pourymovie.repository;

import com.pourymovie.entity.PeopleEntity;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleRepository extends JpaRepository<PeopleEntity, Long> {
  Optional<PeopleEntity> findBySlug(String slug);

  Page<PeopleEntity> findAllByNameEnContainingIgnoreCaseOrNameFaContainingIgnoreCase(
      String nameEn, String nameFa, Pageable pageable);
}
