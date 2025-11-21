package com.pourymovie.controller;

import com.pourymovie.dto.request.CreateSeasonDto;
import com.pourymovie.dto.request.UpdateSeasonDto;
import com.pourymovie.dto.response.SeasonDto;
import com.pourymovie.service.SeasonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/season")
@Tag(name = "Seasons", description = "Endpoints for managing seasons of series titles")
public class SeasonController {
  @Autowired
  SeasonService seasonService;

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Required Role = Admin")
  public SeasonDto createSeason(@Valid @RequestBody CreateSeasonDto seasonDto) {
    return seasonService.createSeason(seasonDto);
  }

  @GetMapping("/{titleId}")
  public List<SeasonDto> getSeasonByTitleId(@PathVariable Long titleId) {
    return seasonService.getAllSeasonsByTitleId(titleId);
  }

  @PatchMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Required Role = Admin")
  public SeasonDto updateSeason(@PathVariable Long id ,@Valid @RequestBody UpdateSeasonDto updateSeasonDto) {
    return seasonService.updateSeason(id,updateSeasonDto);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Required Role = Admin")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteSeasonById(@PathVariable Long id) {
    seasonService.deleteSeasonById(id);
  }
}
