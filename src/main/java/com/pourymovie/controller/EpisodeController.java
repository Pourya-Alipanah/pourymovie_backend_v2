package com.pourymovie.controller;

import com.pourymovie.dto.request.CreateEpisodeDto;
import com.pourymovie.dto.request.UpdateEpisodeDto;
import com.pourymovie.dto.response.EpisodeDto;
import com.pourymovie.service.EpisodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/episode")
@Tag(name = "Episode", description = "Endpoints for managing episodes")
public class EpisodeController {

  @Autowired
  private EpisodeService episodeService;

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Required Role = Admin")
  public EpisodeDto createEpisode(@Valid @RequestBody CreateEpisodeDto createEpisodeDto) {
    return episodeService.create(createEpisodeDto);
  }

  @GetMapping("/{seasonId}")
  public List<EpisodeDto> getEpisode(@PathVariable Long seasonId) {
    return episodeService.getAllEpisodesBySeasonId(seasonId);
  }

  @PatchMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Required Role = Admin")
  public EpisodeDto updateEpisode(@PathVariable Long id, @Valid @RequestBody UpdateEpisodeDto updateEpisodeDto) {
    return episodeService.update(id, updateEpisodeDto);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Required Role = Admin")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteEpisode(@PathVariable Long id) {
    episodeService.deleteById(id);
  }
}
