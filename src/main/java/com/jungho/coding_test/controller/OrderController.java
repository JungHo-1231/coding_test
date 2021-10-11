package com.jungho.coding_test.controller;

import com.jungho.coding_test.dto.CreateResponseDto;
import com.jungho.coding_test.dto.order.CreateOrderRequestDto;
import com.jungho.coding_test.dto.order.OrderDto;
import com.jungho.coding_test.dto.order.OrderSearch;
import com.jungho.coding_test.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문 등록
     *
     * @param createOrderRequestDto
     * @return CreateResponseDto
     */
    @PostMapping("/api/v1/order")
    public CreateResponseDto createOrder(@RequestBody @Validated CreateOrderRequestDto createOrderRequestDto) {

        Long orderId = orderService.createOrder(createOrderRequestDto);
        return new CreateResponseDto(orderId);
    }


    /**
     * 주문 검색
     *
     * @param orderSearch
     * @return
     */
    @PostMapping("/api/v1/orders/search")
    public List<OrderDto> searchOrderWithCondition(@RequestBody OrderSearch orderSearch) {
        return orderService.orderSearch(orderSearch);
    }

    /**
     * 주문 취소
     *
     * @param orderId
     * @return
     */
    @PostMapping("/api/v1/{orderId}/cancel")
    public ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);

        return ResponseEntity.ok().build();
    }
}
