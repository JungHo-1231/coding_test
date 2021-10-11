package com.jungho.coding_test.repository;

import com.jungho.coding_test.dto.member.MemberDto;

import java.util.List;

public interface MemberRepositoryCustom {
    List<MemberDto> findAllMemberUsingQueryDsl();
}
