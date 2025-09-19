package com.assessment.library_managment_system.repository;


import com.assessment.library_managment_system.model.Member;
import com.assessment.library_managment_system.vm.MembershipStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberId(String memberId);
    Optional<Member> findByEmail(String email);
    Page<Member> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName, Pageable pageable);
    Page<Member> findByStatus(MembershipStatus status, Pageable pageable);
}