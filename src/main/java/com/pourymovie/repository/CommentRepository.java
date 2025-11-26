package com.pourymovie.repository;

import com.pourymovie.entity.CommentEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
  Page<CommentEntity> findAllByTitleId(Long titleId ,Pageable pageable);
  List<CommentEntity> findAllByTitleId(Long titleId);

  Page<CommentEntity> findAllByUserId(Long userId ,Pageable pageable);

  boolean existsByIdAndUserId(Long commentId, Long userId);
}
