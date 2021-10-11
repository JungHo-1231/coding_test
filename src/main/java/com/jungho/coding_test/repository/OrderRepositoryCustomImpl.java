package com.jungho.coding_test.repository;

import com.jungho.coding_test.domain.Order;
import com.jungho.coding_test.domain.OrderStatus;
import com.jungho.coding_test.dto.order.OrderDto;
import com.jungho.coding_test.dto.order.OrderSearch;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.jungho.coding_test.domain.QMember.member;
import static com.jungho.coding_test.domain.QOrder.order;

public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    // querydsl
    private final JPAQueryFactory queryFactory;

    public OrderRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Order> findAllByQueryDsl(OrderSearch orderSearch) {
        return queryFactory
                .select(order)
                .from(order)
                .join(order.member, member)
                .where(statusEq(orderSearch.getOrderStatus()),
                        nameLike(orderSearch.getMemberName()))
                .limit(1000)
                .fetch();
    }

    private BooleanExpression statusEq(OrderStatus statusCond) {
        if (statusCond == null) {
            return null;
        }
        return order.status.eq(statusCond);
    }

    private BooleanExpression nameLike(String nameCond) {
        if (!StringUtils.hasText(nameCond)) {
            return null;
        }
        return member.name.like(nameCond);
    }
}
