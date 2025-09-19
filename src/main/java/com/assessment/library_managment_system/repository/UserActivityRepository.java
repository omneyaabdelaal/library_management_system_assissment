package com.assessment.library_managment_system.repository;

import com.assessment.library_managment_system.model.SystemUser;
import com.assessment.library_managment_system.model.UserActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    Page<UserActivity> findByUser(SystemUser user, Pageable pageable);
    Page<UserActivity> findByActionContainingIgnoreCase(String action, Pageable pageable);
}