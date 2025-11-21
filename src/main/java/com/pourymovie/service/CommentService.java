package com.pourymovie.service;

import com.pourymovie.dto.request.CreateCommentDto;
import com.pourymovie.dto.request.UpdateCommentDto;
import com.pourymovie.dto.response.CommentDto;
import com.pourymovie.entity.UserEntity;
import com.pourymovie.mapper.CommentMapper;
import com.pourymovie.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CommentService {

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private CommentMapper commentMapper;

  @Autowired
  private TitleService titleService;

  public CommentDto create(CreateCommentDto commentDto , UserEntity user) {
    var comment = commentMapper.toEntity(commentDto);
    var title = titleService.findById(commentDto.titleId());
    comment.setTitle(title);
    comment.setUser(user);
    var createdComment = commentRepository.save(comment);
    return commentMapper.toDto(createdComment);
  }

  public Page<CommentDto> getAllTitleComments(Long titleId, Pageable pageable) {
    return commentMapper.toDto(commentRepository.findAllByTitleId(titleId, pageable));
  }

  public Page<CommentDto> getAllUserComments(Long userId, Pageable pageable) {
    return commentMapper.toDto(commentRepository.findAllByUserId(userId, pageable));
  }

  public CommentDto update(UpdateCommentDto commentDto , Long commentId) {
    var existingComment = commentRepository.findById(commentId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    commentMapper.updateEntityFromDto(commentDto, existingComment);
    var updatedComment = commentRepository.save(existingComment);
    return commentMapper.toDto(updatedComment);
  }

  public void delete(Long commentId) {
    var existingComment = commentRepository.existsById(commentId);
    if (!existingComment) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    commentRepository.deleteById(commentId);
  }
}
