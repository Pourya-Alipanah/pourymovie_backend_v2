package com.pourymovie.controller;

import com.pourymovie.dto.request.SignUpDto;
import com.pourymovie.dto.request.UpdateUserDto;
import com.pourymovie.dto.response.UserDto;
import com.pourymovie.entity.UserEntity;
import com.pourymovie.enums.UserRole;
import com.pourymovie.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Endpoints for user management and create admin users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PreAuthorize("hasRole('SUPER_ADMIN')")
  @Operation(summary = "Required Role = Super Admin")
  @PostMapping("/admin")
  @ResponseStatus(HttpStatus.CREATED)
  public UserDto addUser(@Valid @RequestBody SignUpDto signUpDto) throws Exception {
    return userService.createUser(signUpDto, UserRole.ADMIN);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  @Operation(summary = "Required Role = Admin")
  @PageableAsQueryParam
  public Page<UserDto> getUsers(@Parameter(hidden = true) Pageable pageable) {
    return userService.getUsers(pageable);
  }

  @GetMapping("/current")
  public UserDto getCurrentUser() {
    return userService.getCurrentUser();
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/{id}")
  @Operation(summary = "Required Role = Admin")
  public UserDto getUserById(@PathVariable Long id) {
    return userService.getUserById(id);
  }

  @PatchMapping("/current")
  public UserDto updateCurrentUser(@Valid @RequestBody UpdateUserDto userDto) throws Exception {
    return userService.updateCurrentUser(userDto);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PatchMapping("/{id}")
  @Operation(summary = "Required Role = Admin")
  public UserDto updateUser(@Valid @RequestBody UpdateUserDto userDto, @PathVariable Long id)
      throws Exception {
    return userService.updateUserById(id, userDto);
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCurrentUser() {
    userService.deleteCurrentUser();
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  @Operation(summary = "Required Role = Admin")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCurrentUser(@PathVariable Long id) {
    userService.deleteUserById(id);
  }
}
