package com.jungho.coding_test.repository;

import com.jungho.coding_test.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> , OrderRepositoryCustom {
}
