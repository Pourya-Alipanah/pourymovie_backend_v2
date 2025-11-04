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
  @PostMapping("/admin")
  @Operation(summary = "just for super admin user to create admin users")
  public ResponseEntity<UserEntity> addUser(@RequestBody SignUpDto signUpDto) {
    return new ResponseEntity<>(userService.createUser(signUpDto, UserRole.ADMIN) , HttpStatus.CREATED);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public Page<UserEntity> getUsers(Pageable pageable) {
    return userService.getUsers(pageable);
  }

  @GetMapping("/current")
  public ResponseEntity<UserEntity> getCurrentUser() {
    return new ResponseEntity<>(userService.getCurrentUser(), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserEntity> getUserById(@PathVariable String id) {
    return new ResponseEntity<>(userService.getUserById(Long.parseLong(id)), HttpStatus.OK);
  }

  /*public ResponseEntity<UserEntity> updateCurrentUser(@Valid @RequestBody UpdateUserDto userDto) {
    return new ResponseEntity<>(userService.updateCurrentUser(userDto), HttpStatus.OK);
  }*/

  @DeleteMapping
  @ApiResponse(responseCode = "204")
  public ResponseEntity<String> deleteCurrentUser() {
    userService.deleteCurrentUser();
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
