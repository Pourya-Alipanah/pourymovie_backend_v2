package com.pourymovie.security.permission;

import com.pourymovie.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("commentSecurity")
public class CommentSecurity {

  @Autowired private CommentRepository commentRepository;

  public boolean isCommentOwner(Long commentId, Long userId) {
    return commentRepository.existsByIdAndUserId(commentId, userId);
  }
}
