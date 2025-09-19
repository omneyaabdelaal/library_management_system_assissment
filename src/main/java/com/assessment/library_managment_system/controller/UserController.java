package com.assessment.library_managment_system.controller;


import com.assessment.library_managment_system.dto.UserDto;
import com.assessment.library_managment_system.model.UserActivity;
import com.assessment.library_managment_system.service.impl.UserActivityService;
import com.assessment.library_managment_system.service.impl.UserService;
import com.assessment.library_managment_system.vm.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserActivityService userActivityService;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Page<UserDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<UserDto> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR') or (hasRole('LIBRARIAN') and #id == authentication.principal.id)")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        UserDto user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<UserDto> createUser(
            @RequestBody UserDto userDto,
            @RequestParam String password) {

        UserDto createdUser = userService.createUser(userDto, password);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR') or (hasRole('LIBRARIAN') and #id == authentication.principal.id)")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/password")
    @PreAuthorize("hasRole('ADMINISTRATOR') or #id == authentication.principal.id")
    public ResponseEntity<UserDto> updatePassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> passwordData) {

        String oldPassword = passwordData.get("oldPassword");
        String newPassword = passwordData.get("newPassword");

        UserDto updatedUser = userService.updateUserPassword(id, oldPassword, newPassword);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<UserDto> updateUserRoles(
            @PathVariable Long id,
            @RequestBody Set<UserRole> roles) {

        UserDto updatedUser = userService.updateUserRoles(id, roles);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{id}/toggle-status")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<UserDto> toggleUserStatus(@PathVariable Long id) {
        UserDto updatedUser = userService.toggleUserStatus(id);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/{id}/activities")
    @PreAuthorize("hasRole('ADMINISTRATOR') or #id == authentication.principal.id")
    public ResponseEntity<Page<UserActivity>> getUserActivities(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<UserActivity> activities = userActivityService.getActivitiesByUser(id, pageable);
        return ResponseEntity.ok(activities);
    }
}

