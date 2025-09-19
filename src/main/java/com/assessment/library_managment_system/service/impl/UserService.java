package com.assessment.library_managment_system.service.impl;


import com.assessment.library_managment_system.dto.UserDto;
import com.assessment.library_managment_system.exception.ResourceNotFoundException;
import com.assessment.library_managment_system.model.Role;
import com.assessment.library_managment_system.model.SystemUser;
import com.assessment.library_managment_system.repository.RoleRepository;
import com.assessment.library_managment_system.repository.SystemUserRepository;
import com.assessment.library_managment_system.vm.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private SystemUserRepository systemUserRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<UserDto> getAllUsers(Pageable pageable) {
        return systemUserRepository.findAll(pageable).map(this::convertToDto);
    }

    public UserDto getUserById(Long id) {
        SystemUser user = systemUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return convertToDto(user);
    }

    public UserDto getUserByUsername(String username) {
        SystemUser user = systemUserRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return convertToDto(user);
    }

    public UserDto createUser(UserDto userDto, String password) {
        // Check if username or email already exists
        if (systemUserRepository.existsByUsername(userDto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (systemUserRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        SystemUser user = convertToEntity(userDto);
        user.setPassword(passwordEncoder.encode(password));

        // Set default role if no roles specified
        if (user.getRoles().isEmpty()) {
            Role staffRole = roleRepository.findByName(UserRole.ROLE_STAFF)
                    .orElseThrow(() -> new ResourceNotFoundException("Default role not found"));
            user.getRoles().add(staffRole);
        }

        SystemUser savedUser = systemUserRepository.save(user);
        return convertToDto(savedUser);
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        SystemUser existingUser = systemUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        updateUserEntity(existingUser, userDto);
        SystemUser updatedUser = systemUserRepository.save(existingUser);
        return convertToDto(updatedUser);
    }

    public void deleteUser(Long id) {
        if (!systemUserRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        systemUserRepository.deleteById(id);
    }

    public UserDto updateUserPassword(Long id, String oldPassword, String newPassword) {
        SystemUser user = systemUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        SystemUser updatedUser = systemUserRepository.save(user);
        return convertToDto(updatedUser);
    }

    public UserDto updateUserRoles(Long id, Set<UserRole> roleNames) {
        SystemUser user = systemUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        Set<Role> roles = new HashSet<>();
        for (UserRole roleName : roleNames) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));
            roles.add(role);
        }

        user.setRoles(roles);
        SystemUser updatedUser = systemUserRepository.save(user);
        return convertToDto(updatedUser);
    }

    public UserDto toggleUserStatus(Long id) {
        SystemUser user = systemUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setEnabled(!user.isEnabled());
        SystemUser updatedUser = systemUserRepository.save(user);
        return convertToDto(updatedUser);
    }

    private UserDto convertToDto(SystemUser user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEnabled(user.isEnabled());
        dto.setRoles(user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet()));
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    private SystemUser convertToEntity(UserDto dto) {
        SystemUser user = new SystemUser();
        updateUserEntity(user, dto);
        return user;
    }

    private void updateUserEntity(SystemUser user, UserDto dto) {
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEnabled(dto.isEnabled());

        // Handle roles
        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            for (String roleName : dto.getRoles()) {
                Role role = roleRepository.findByName(UserRole.valueOf(roleName))
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));
                roles.add(role);
            }
            user.setRoles(roles);
        }
    }
}
