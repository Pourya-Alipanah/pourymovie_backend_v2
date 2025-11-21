package com.pourymovie.controller;

import com.pourymovie.dto.request.CreateCommentDto;
import com.pourymovie.dto.request.UpdateCommentDto;
import com.pourymovie.dto.response.CommentDto;
import com.pourymovie.security.userDetails.CustomUserDetails;
import com.pourymovie.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comment")
@Tag(name = "comment", description = "Endpoints for managing comments")
public class CommentController {

  @Autowired
  private CommentService commentService;

  @PostMapping
  public CommentDto createComment(
          @Valid @RequestBody CreateCommentDto commentDto,
          @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    return commentService.create(commentDto, userDetails.user());
  }

  @GetMapping
  @PageableAsQueryParam
  public Page<CommentDto> getCurrentUserComments(
          @AuthenticationPrincipal CustomUserDetails userDetails,
          @Parameter(hidden = true) Pageable pageable
  ) {
    return commentService.getAllUserComments(userDetails.user().getId(), pageable);
  }

  @GetMapping("/{titleId}")
  @PageableAsQueryParam
  public Page<CommentDto> getTitleComments(
          @PathVariable Long titleId,
          @Parameter(hidden = true) Pageable pageable
  ) {
    return commentService.getAllTitleComments(titleId, pageable);
  }

  @PatchMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN') or @commentSecurity.isCommentOwner(#id, #userDetails.user().id)")
  @Operation(summary = "Just For Comment's Owner Or Admin")
  public CommentDto updateComment(
          @Valid @RequestBody UpdateCommentDto commentDto,
          @PathVariable Long id,
          @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    return commentService.update(commentDto, id);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN') or @commentSecurity.isCommentOwner(#id, #userDetails.user().id)")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Just For Comment's Owner Or Admin")
  public void deleteComment(
          @PathVariable Long id,
          @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    commentService.delete(id);
  }
}
