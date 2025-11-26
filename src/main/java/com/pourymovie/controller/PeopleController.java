package com.pourymovie.controller;

import com.pourymovie.dto.request.CreatePeopleDto;
import com.pourymovie.dto.request.UpdatePeopleDto;
import com.pourymovie.dto.response.PeopleDetailsDto;
import com.pourymovie.dto.response.PeopleDto;
import com.pourymovie.service.PeopleService;
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
@RequestMapping("/people")
@Tag(name = "People", description = "Endpoints for managing people")
public class PeopleController {
  @Autowired private PeopleService peopleService;

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Required Role = Admin")
  public PeopleDetailsDto createPeople(@Valid @RequestBody CreatePeopleDto createPeopleDto)
      throws Exception {
    return peopleService.create(createPeopleDto);
  }

  @GetMapping
  @PageableAsQueryParam
  public Page<PeopleDto> findAll(@Parameter(hidden = true) Pageable pageable) {
    return peopleService.findAll(pageable);
  }

  @GetMapping("/{slug}")
  public PeopleDetailsDto findBySlug(@PathVariable String slug) {
    return peopleService.findBySlug(slug);
  }

  @PatchMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Required Role = Admin")
  public PeopleDetailsDto updatePeople(
      @Valid @RequestBody UpdatePeopleDto updatePeopleDto, @PathVariable Long id) throws Exception {
    return peopleService.update(updatePeopleDto, id);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Required Role = Admin")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deletePeople(@PathVariable Long id) {
    peopleService.deleteById(id);
  }
}
