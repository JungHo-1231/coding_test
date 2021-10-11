package com.jungho.coding_test.dto.order;

import lombok.Data;

@Data
public class OrderItemDto {

    private Long id;  // orderItem pk
    private int orderPrice; // 주문 가격
    private int count; // 주문 수량
    private String itemName; // 상품 이름
}
