package com.jungho.coding_test.repository;

import com.jungho.coding_test.dto.member.AddressDto;
import com.jungho.coding_test.dto.member.MemberDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;

import static com.jungho.coding_test.domain.QMember.member;

public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    // querydsl
    private final JPAQueryFactory queryFactory;

    @Autowired
    public MemberRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MemberDto> findAllMemberUsingQueryDsl() {
        return queryFactory
                .select(
                        Projections.constructor(MemberDto.class,
                                member.id,
                                member.name,
                                Projections.constructor(AddressDto.class,
                                        member.address.city,
                                        member.address.street,
                                        member.address.zipcode
                                )
                        ))
                .from(member)
                .fetch();
    }
}
