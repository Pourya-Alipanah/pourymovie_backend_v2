package com.pourymovie.repository;

import com.pourymovie.entity.PeopleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<PeopleEntity , Long> {
  Optional<PeopleEntity> findBySlug(String slug);
}
