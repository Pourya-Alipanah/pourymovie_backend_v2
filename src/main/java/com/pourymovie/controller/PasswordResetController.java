package com.pourymovie.controller;

import com.pourymovie.dto.request.PasswordResetConfirmDto;
import com.pourymovie.dto.request.PasswordResetDto;
import com.pourymovie.dto.response.Message;
import com.pourymovie.service.PasswordResetService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/password-reset")
@RequiredArgsConstructor
@Tag(name = "Password Reset", description = "Endpoints for password reset")
public class PasswordResetController {

  private final PasswordResetService passwordResetService;

  @PostMapping("/request")
  @ResponseStatus(HttpStatus.ACCEPTED)
  @ApiResponse(
      responseCode = "202",
      description = "Accept the request and send an email within seconds to user")
  public Message requestReset(@Valid @RequestBody PasswordResetDto passwordResetDto) {
    return passwordResetService.requestPasswordReset(passwordResetDto);
  }

  @PostMapping("/confirm")
  public Message confirmReset(@Valid @RequestBody PasswordResetConfirmDto passwordResetConfirmDto) {
    return passwordResetService.confirmPasswordReset(passwordResetConfirmDto);
  }
}
