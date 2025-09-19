package com.assessment.library_managment_system.service.impl;

import com.assessment.library_managment_system.exception.ResourceNotFoundException;
import com.assessment.library_managment_system.model.SystemUser;
import com.assessment.library_managment_system.model.UserActivity;
import com.assessment.library_managment_system.repository.SystemUserRepository;
import com.assessment.library_managment_system.repository.UserActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserActivityService {

    @Autowired
    private UserActivityRepository userActivityRepository;

    @Autowired
    private SystemUserRepository systemUserRepository;

    public void logActivity(Long userId, String action, String description, String ipAddress) {
        SystemUser user = systemUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        UserActivity activity = new UserActivity();
        activity.setUser(user);
        activity.setAction(action);
        activity.setDescription(description);
        activity.setIpAddress(ipAddress);

        userActivityRepository.save(activity);
    }

    public Page<UserActivity> getActivitiesByUser(Long userId, Pageable pageable) {
        SystemUser user = systemUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return userActivityRepository.findByUser(user, pageable);
    }

    public Page<UserActivity> searchActivities(String action, Pageable pageable) {
        return userActivityRepository.findByActionContainingIgnoreCase(action, pageable);
    }

    public Page<UserActivity> getAllActivities(Pageable pageable) {
        return userActivityRepository.findAll(pageable);
    }
}

