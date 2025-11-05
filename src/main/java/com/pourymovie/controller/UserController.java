package com.pourymovie.controller;

import com.pourymovie.dto.SignUpDto;
import com.pourymovie.dto.UpdateUserDto;
import com.pourymovie.entity.UserEntity;
import com.pourymovie.enums.UserRole;
import com.pourymovie.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
public class UserController {

  @Autowired
  private UserService userService;

  @PreAuthorize("hasRole('SUPER_ADMIN')")
  @Operation(summary = "Required Role = Super Admin")
  @PostMapping("/admin")
  public ResponseEntity<UserEntity> addUser(@Valid @RequestBody SignUpDto signUpDto) {
    return new ResponseEntity<>(userService.createUser(signUpDto, UserRole.ADMIN) , HttpStatus.CREATED);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  @Operation(summary = "Required Role = Admin")
  public Page<UserEntity> getUsers(Pageable pageable) {
    return userService.getUsers(pageable);
  }

  @GetMapping("/current")
  public UserEntity getCurrentUser() {
    return userService.getCurrentUser();
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/{id}")
  @Operation(summary = "Required Role = Admin")
  public UserEntity getUserById(@PathVariable Long id) {
    return userService.getUserById(id);
  }

  @PatchMapping("/current")
  public UserEntity updateCurrentUser(@Valid @RequestBody UpdateUserDto userDto) {
    return userService.updateCurrentUser(userDto);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PatchMapping("/{id}")
  @Operation(summary = "Required Role = Admin")
  public UserEntity updateUser(@Valid @RequestBody UpdateUserDto userDto , @PathVariable Long id) {
    return userService.updateUserById(id , userDto);
  }

  @DeleteMapping
  @ApiResponse(responseCode = "204")
  public ResponseEntity<String> deleteCurrentUser() {
    userService.deleteCurrentUser();
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  @Operation(summary = "Required Role = Admin")
  @ApiResponse(responseCode = "204")
  public ResponseEntity<String> deleteCurrentUser(@PathVariable Long id) {
    userService.deleteUserById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
