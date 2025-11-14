package com.pourymovie.controller;

import com.pourymovie.dto.request.CreateGenreDto;
import com.pourymovie.dto.request.UpdateGenreDto;
import com.pourymovie.dto.response.GenreDto;
import com.pourymovie.service.GenreService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/genre")
@Tag(name = "Genres", description = "Endpoints for managing genres")
public class GenreController {

  @Autowired
  private GenreService genreService;

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Required Role = Admin")
  public GenreDto createGenre(@Valid @RequestBody CreateGenreDto createGenreDto) {
    return genreService.create(createGenreDto);
  }

  @GetMapping
  @PageableAsQueryParam
  public Page<GenreDto> findAll(@Parameter(hidden = true) Pageable pageable) {
    return genreService.findAll(pageable);
  }

  @GetMapping("/{slug}")
  public GenreDto findBySlug(@PathVariable String slug) {
    return genreService.findBySlug(slug);
  }

  @PatchMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Required Role = Admin")
  public GenreDto updateGenre(@PathVariable Long id, @Valid @RequestBody UpdateGenreDto updateGenreDto) {
    return genreService.update(id, updateGenreDto);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Required Role = Admin")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteGenre(@PathVariable Long id) {
    genreService.delete(id);
  }
}
