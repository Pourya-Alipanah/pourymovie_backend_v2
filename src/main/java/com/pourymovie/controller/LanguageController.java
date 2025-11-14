package com.pourymovie.controller;

import com.pourymovie.dto.request.CreateLanguageDto;
import com.pourymovie.dto.request.UpdateLanguageDto;
import com.pourymovie.dto.response.LanguageDto;
import com.pourymovie.service.LanguageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/language")
@Tag(name = "Languages", description = "Endpoints for managing languages")
public class LanguageController {
  @Autowired
  private LanguageService languageService;

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Required Role = Admin")
  public LanguageDto createLanguage(@Valid @RequestBody CreateLanguageDto createLanguageDto) {
    return languageService.create(createLanguageDto);
  }

  @GetMapping
  public Page<LanguageDto> getAll(Pageable pageable) {
    return languageService.getAll(pageable);
  }

  @GetMapping("/{slug}")
  public LanguageDto getBySlug(@PathVariable String slug) {
    return languageService.getBySlug(slug);
  }

  @PatchMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Required Role = Admin")
  public LanguageDto updateLanguage(@PathVariable Long id ,@Valid @RequestBody UpdateLanguageDto updateLanguageDto) {
    return languageService.update(id, updateLanguageDto);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Required Role = Admin")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteLanguage(@PathVariable Long id) {
    languageService.deleteById(id);
  }
}
