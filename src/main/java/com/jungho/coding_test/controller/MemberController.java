package com.jungho.coding_test.controller;

import com.jungho.coding_test.domain.Member;
import com.jungho.coding_test.dto.*;
import com.jungho.coding_test.dto.member.CreateMemberRequestDto;
import com.jungho.coding_test.dto.CreateResponseDto;
import com.jungho.coding_test.dto.member.MemberDto;
import com.jungho.coding_test.repository.MemberRepository;
import com.jungho.coding_test.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final MemberService memberService;

    /**
     * 전체 회원 조회을 조회한다.
     *
     * @return List<MemberDto>
     */
    @GetMapping("/api/v1/members")
    public List<MemberDto> getAllMembers() {
        List<MemberDto> allMemberUsingQueryDsl = memberRepository.findAllMemberUsingQueryDsl();
        return allMemberUsingQueryDsl;
    }

    /**
     * id(pk) 값으로 회원을 조회한다.
     *
     * @param id
     * @return MemberDto
     */
    @GetMapping("/api/v1/member/{id}")
    public MemberDto findMember(@PathVariable("id") Long id) {
        Member findMember = memberRepository.findById(id).orElseThrow(() -> new IllegalStateException("회원을 찾을 수 없습니다"));
        MemberDto memberDto = modelMapper.map(findMember, MemberDto.class);
        return memberDto;
    }


    /**
     * 회원을 생성한다.
     *
     * @param request
     * @return CreateMemberResponseDto
     */
    @PostMapping("/api/v1/member")
    public CreateResponseDto createMember(@RequestBody @Validated CreateMemberRequestDto request) {

        Member member = modelMapper.map(request, Member.class);
        Long id = memberService.join(member);

        return new CreateResponseDto(id);
    }



}

