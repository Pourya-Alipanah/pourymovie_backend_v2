package com.pourymovie.repository;

import com.pourymovie.entity.RefreshTokenEntity;
import com.pourymovie.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity,Long> {
  @Transactional
  void deleteByUser(UserEntity user);

  @Transactional
  Optional<RefreshTokenEntity> findByToken(String token);

  @Transactional
  void deleteByToken(String token);
}
