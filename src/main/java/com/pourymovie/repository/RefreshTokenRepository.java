package com.pourymovie.repository;

import com.pourymovie.entity.RefreshTokenEntity;
import com.pourymovie.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity,Long> {
  void deleteByUser(UserEntity user);

  Optional<RefreshTokenEntity> findByToken(String token);
}
