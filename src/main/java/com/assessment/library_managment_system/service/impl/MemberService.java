package com.assessment.library_managment_system.service.impl;

import com.assessment.library_managment_system.dto.MemberDto;
import com.assessment.library_managment_system.exception.ResourceNotFoundException;
import com.assessment.library_managment_system.model.Member;
import com.assessment.library_managment_system.repository.MemberRepository;
import com.assessment.library_managment_system.vm.MembershipStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public Page<MemberDto> getAllMembers(Pageable pageable) {
        return memberRepository.findAll(pageable).map(this::convertToDto);
    }

    public MemberDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
        return convertToDto(member);
    }

    public MemberDto getMemberByMemberId(String memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with member ID: " + memberId));
        return convertToDto(member);
    }

    public Page<MemberDto> searchMembersByName(String name, Pageable pageable) {
        return memberRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                name, name, pageable).map(this::convertToDto);
    }

    public Page<MemberDto> getMembersByStatus(MembershipStatus status, Pageable pageable) {
        return memberRepository.findByStatus(status, pageable).map(this::convertToDto);
    }

    public MemberDto createMember(MemberDto memberDto) {
        Member member = convertToEntity(memberDto);
        member.setMemberId(generateMemberId());
        Member savedMember = memberRepository.save(member);
        return convertToDto(savedMember);
    }

    public MemberDto updateMember(Long id, MemberDto memberDto) {
        Member existingMember = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));

        updateMemberEntity(existingMember, memberDto);
        Member updatedMember = memberRepository.save(existingMember);
        return convertToDto(updatedMember);
    }

    public void deleteMember(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new ResourceNotFoundException("Member not found with id: " + id);
        }
        memberRepository.deleteById(id);
    }

    public MemberDto updateMemberStatus(Long id, MembershipStatus status) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));

        member.setStatus(status);
        Member updatedMember = memberRepository.save(member);
        return convertToDto(updatedMember);
    }

    private String generateMemberId() {
        return "LIB" + System.currentTimeMillis();
    }

    private MemberDto convertToDto(Member member) {
        MemberDto dto = new MemberDto();
        dto.setId(member.getId());
        dto.setMemberId(member.getMemberId());
        dto.setFirstName(member.getFirstName());
        dto.setLastName(member.getLastName());
        dto.setEmail(member.getEmail());
        dto.setPhone(member.getPhone());
        dto.setAddress(member.getAddress());
        dto.setDateOfBirth(member.getDateOfBirth());
        dto.setStatus(member.getStatus());
        dto.setMembershipDate(member.getMembershipDate());
        return dto;
    }

    private Member convertToEntity(MemberDto dto) {
        Member member = new Member();
        updateMemberEntity(member, dto);
        return member;
    }

    private void updateMemberEntity(Member member, MemberDto dto) {
        member.setFirstName(dto.getFirstName());
        member.setLastName(dto.getLastName());
        member.setEmail(dto.getEmail());
        member.setPhone(dto.getPhone());
        member.setAddress(dto.getAddress());
        member.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getStatus() != null) {
            member.setStatus(MembershipStatus.valueOf(dto.getStatus().name()));
        }
    }
}