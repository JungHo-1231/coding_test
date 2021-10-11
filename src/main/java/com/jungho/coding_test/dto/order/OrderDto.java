package com.jungho.coding_test.dto.order;

import com.jungho.coding_test.domain.OrderStatus;
import com.jungho.coding_test.dto.member.MemberDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    // order pk
    private Long id;
    // 주문 날짜
    private LocalDateTime orderDate;
    // 주문한 회원 정보
    private MemberDto member;
    // 주문 아이템
    private List<OrderItemDto> orderItems;
    // 주문 상태
    private OrderStatus orderStatus;
}
