package com.jungho.coding_test.service;

import com.jungho.coding_test.domain.*;
import com.jungho.coding_test.domain.item.Item;
import com.jungho.coding_test.dto.order.CreateOrderRequestDto;
import com.jungho.coding_test.dto.order.OrderDto;
import com.jungho.coding_test.dto.order.OrderSearch;
import com.jungho.coding_test.repository.ItemRepository;
import com.jungho.coding_test.repository.MemberRepository;
import com.jungho.coding_test.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;

    /**
     * 주문 생성
     *
     * @param createOrderRequestDto
     */
    public Long createOrder(CreateOrderRequestDto createOrderRequestDto) {
        //  member 엔티티 조회
        Member findMember = memberRepository.findById(createOrderRequestDto.getMemberId())
                .orElseThrow(() -> new IllegalStateException("회원을 찾을 수 없습니다"));

        // item 엔티티 조회
        Item findItem = itemRepository.findById(createOrderRequestDto.getItemId())
                .orElseThrow(() -> new IllegalStateException("아이템을 찾을 수 없습니다"));

        // 배송 정보 생성
        Address address = modelMapper.map(createOrderRequestDto, Address.class);
        Delivery delivery = Delivery.createDelivery(address, DeliveryStatus.READY);


        // 상품 주문 생성
        OrderItem orderItem = OrderItem.createOrderItem(findItem, findItem.getPrice(), createOrderRequestDto.getCount());

        // 주문 생성
        Order order = Order.createOrder(findMember, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 주문 조회
     * - 이름으로 조회
     * - 주문 상태로 조회
     *
     * @param orderSearch
     * @return List<OrderDto>
     */
    public List<OrderDto> orderSearch(OrderSearch orderSearch) {
        List<Order> orders = orderRepository.findAllByQueryDsl(orderSearch);

        List<OrderDto> orderDtos = orders.stream()
                .map(o -> modelMapper.map(o, OrderDto.class))
                .collect(Collectors.toList());

        return orderDtos;
    }


    /**
     * 주문 취소
     * @param orderId
     */
    public void cancelOrder(Long orderId) {

        // 주문 엔티티 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("주문을 찾을 수 없습니다"));

        // 주문 취소
        order.cancel();
    }

}
