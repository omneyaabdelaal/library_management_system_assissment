package com.assessment.library_managment_system.controller;


import com.assessment.library_managment_system.dto.MemberDto;
import com.assessment.library_managment_system.service.impl.MemberService;
import com.assessment.library_managment_system.vm.MembershipStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "*")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping
    @PreAuthorize("hasRole('STAFF') or hasRole('LIBRARIAN') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<Page<MemberDto>> getAllMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<MemberDto> members = memberService.getAllMembers(pageable);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('STAFF') or hasRole('LIBRARIAN') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<MemberDto> getMemberById(@PathVariable Long id) {
        MemberDto member = memberService.getMemberById(id);
        return ResponseEntity.ok(member);
    }

    @GetMapping("/member-id/{memberId}")
    @PreAuthorize("hasRole('STAFF') or hasRole('LIBRARIAN') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<MemberDto> getMemberByMemberId(@PathVariable String memberId) {
        MemberDto member = memberService.getMemberByMemberId(memberId);
        return ResponseEntity.ok(member);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('STAFF') or hasRole('LIBRARIAN') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<Page<MemberDto>> searchMembersByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<MemberDto> members = memberService.searchMembersByName(name, pageable);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('STAFF') or hasRole('LIBRARIAN') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<Page<MemberDto>> getMembersByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        MembershipStatus memberStatus = MembershipStatus.valueOf(status.toUpperCase());
        Page<MemberDto> members = memberService.getMembersByStatus(memberStatus, pageable);
        return ResponseEntity.ok(members);
    }

    @PostMapping
    @PreAuthorize("hasRole('STAFF') or hasRole('LIBRARIAN') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<MemberDto> createMember(@RequestBody MemberDto memberDto) {
        MemberDto createdMember = memberService.createMember(memberDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMember);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STAFF') or hasRole('LIBRARIAN') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<MemberDto> updateMember(@PathVariable Long id, @RequestBody MemberDto memberDto) {
        MemberDto updatedMember = memberService.updateMember(id, memberDto);
        return ResponseEntity.ok(updatedMember);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<MemberDto> updateMemberStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        MembershipStatus memberStatus =MembershipStatus.valueOf(status.toUpperCase());
        MemberDto updatedMember = memberService.updateMemberStatus(id, memberStatus);
        return ResponseEntity.ok(updatedMember);
    }
}