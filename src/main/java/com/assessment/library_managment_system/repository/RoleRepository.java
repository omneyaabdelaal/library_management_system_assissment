package com.assessment.library_managment_system.repository;

import com.assessment.library_managment_system.model.Role;
import com.assessment.library_managment_system.vm.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(UserRole name);
}