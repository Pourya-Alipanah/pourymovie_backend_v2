package com.pourymovie.controller;

import com.pourymovie.dto.request.CreateMultipleVideoLinkDto;
import com.pourymovie.dto.request.CreateVideoLinkDto;
import com.pourymovie.dto.request.UpdateVideoLinkDto;
import com.pourymovie.dto.response.VideoLinkDto;
import com.pourymovie.service.VideoLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/video-link")
@Tag(name = "Video Link", description = "Endpoints for managing video links")
public class VideoLinkController {
  @Autowired
  private VideoLinkService videoLinkService;

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Required Role = Admin")
  public VideoLinkDto createVideoLink(@Valid @RequestBody CreateVideoLinkDto createVideoLinkDto) throws Exception {
    return videoLinkService.create(createVideoLinkDto);
  }

  @PostMapping("/multi")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Required Role = Admin")
  public List<VideoLinkDto> createMultipleVideoLinks(@Valid @RequestBody CreateMultipleVideoLinkDto createMultipleVideoLinkDto) {
    return videoLinkService.createMultiple(createMultipleVideoLinkDto);
  }

  @GetMapping("/by-title/{titleId}")
  public List<VideoLinkDto> getVideoLinksByTitleId(@PathVariable Long titleId) throws Exception {
    return videoLinkService.getByTitleId(titleId);
  }

  @GetMapping("/by-episode/{episodeId}")
  public List<VideoLinkDto> getVideoLinksByEpisodeId(@PathVariable Long episodeId) throws Exception {
    return videoLinkService.getByEpisodeId(episodeId);
  }

  @PatchMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Required Role = Admin")
  public VideoLinkDto updateVideoLink(@PathVariable Long id, @Valid @RequestBody UpdateVideoLinkDto updateVideoLinkDto) throws Exception {
    return videoLinkService.update(updateVideoLinkDto, id);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Required Role = Admin")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteVideoLink(@PathVariable Long id) {
    videoLinkService.deleteById(id);
  }
}
