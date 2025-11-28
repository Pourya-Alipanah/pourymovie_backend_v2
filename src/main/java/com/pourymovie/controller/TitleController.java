package com.pourymovie.controller;

import com.pourymovie.dto.request.CreateTitleDto;
import com.pourymovie.dto.request.TitleFilterDto;
import com.pourymovie.dto.request.UpdateTitleDto;
import com.pourymovie.dto.response.TitleDetailsDto;
import com.pourymovie.dto.response.TitleDto;
import com.pourymovie.service.TitleService;
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
@RequestMapping("/titles")
@Tag(name = "Titles", description = "Endpoints for managing movie and series titles")
public class TitleController {

  @Autowired private TitleService titleService;

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Required Role = Admin")
  public TitleDetailsDto createTitle(@Valid @RequestBody CreateTitleDto createTitleDto)
      throws Exception {
    return titleService.create(createTitleDto);
  }

  @PostMapping("/filter")
  @PageableAsQueryParam
  public Page<TitleDto> findAllWithFilter(
      @Parameter(hidden = true) Pageable pageable, @RequestBody TitleFilterDto filters) {
    return titleService.findAll(filters, pageable);
  }

  @GetMapping
  @PageableAsQueryParam
  public Page<TitleDto> findAll(@Parameter(hidden = true) Pageable pageable) {
    return titleService.findAll(pageable);
  }

  @GetMapping("/{slug}")
  public TitleDetailsDto findBySlug(@PathVariable String slug) {
    return titleService.findBySlug(slug);
  }

  @PatchMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Required Role = Admin")
  public TitleDetailsDto updateTitle(
      @PathVariable Long id, @Valid @RequestBody UpdateTitleDto updateTitleDto) throws Exception {
    return titleService.update(updateTitleDto, id);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Required Role = Admin")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteById(@PathVariable Long id) {
    titleService.deleteById(id);
  }
}
