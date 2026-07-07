package com.pourymovie.security.permission;

import com.pourymovie.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("commentSecurity")
@RequiredArgsConstructor
public class CommentSecurity {

  private final CommentRepository commentRepository;

  public boolean isCommentOwner(Long commentId, Long userId) {
    return commentRepository.existsByIdAndUserId(commentId, userId);
  }
}
