package com.jungho.coding_test.repository;

import com.jungho.coding_test.domain.Order;
import com.jungho.coding_test.dto.order.OrderDto;
import com.jungho.coding_test.dto.order.OrderSearch;

import java.util.List;

public interface OrderRepositoryCustom {
    List<Order> findAllByQueryDsl(OrderSearch orderSearch);
}
