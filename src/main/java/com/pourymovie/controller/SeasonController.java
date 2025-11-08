package com.pourymovie.controller;

import com.pourymovie.dto.response.SeasonDto;
import com.pourymovie.service.SeasonService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/season")
public class SeasonController {
  @Autowired
  SeasonService seasonService;

  @GetMapping("/{titleId}")
  public List<SeasonDto> getSeasonByTitleId(@PathVariable Long titleId) {
    return seasonService.getAllSeasonsByTitleId(titleId);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Required Role = Admin")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteSeasonById(@PathVariable Long id) {
    seasonService.deleteSeasonById(id);
  }
}
