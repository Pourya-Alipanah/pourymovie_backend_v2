package com.pourymovie.controller;

import com.pourymovie.dto.response.TitleDetailsDto;
import com.pourymovie.dto.response.TitleDto;
import com.pourymovie.service.TitleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/titles")
@Tag(name = "Titles", description = "Endpoints for managing movie and series titles")
public class TitleController {

  @Autowired
  private TitleService titleService;

  @GetMapping
  public Page<TitleDto> findAll(Pageable pageable) {
    return titleService.findAll(pageable);
  }

  @GetMapping("/{slug}")
  public TitleDetailsDto findBySlug(@PathVariable String slug) {
    return titleService.findBySlug(slug);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Required Role = Admin")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteById(@PathVariable Long id) {
    titleService.deleteById(id);
  }
}
