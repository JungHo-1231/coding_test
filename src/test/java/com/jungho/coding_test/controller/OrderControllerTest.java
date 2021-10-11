package com.jungho.coding_test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jungho.coding_test.domain.*;
import com.jungho.coding_test.domain.item.Book;
import com.jungho.coding_test.dto.item.CreateBookRequestDto;
import com.jungho.coding_test.dto.order.CreateOrderRequestDto;
import com.jungho.coding_test.dto.order.OrderSearch;
import com.jungho.coding_test.repository.ItemRepository;
import com.jungho.coding_test.repository.MemberRepository;
import com.jungho.coding_test.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("주문 생성 테스트")
    void 주문_등록_테스트() throws Exception {
        //given
        Member member1 = createMember("jung", "서울", "123", "456");
        Member savedMember = memberRepository.save(member1);

        Book book1 = createBook("코딩", 10_000, 10, "1234", "위키북스");
        Book savedItem = itemRepository.save(book1);

        CreateOrderRequestDto createOrderRequestDto = getCreateOrderRequestDto(savedMember, savedItem);

        //when
        //then
        mockMvc.perform(post("/api/v1/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderRequestDto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists());
    }


    @Test
    @DisplayName("주문 생성 실패 테스트")
    void 주문_등록_실패_테스트() throws Exception {

        //given
        CreateOrderRequestDto createOrderRequestDto = new CreateOrderRequestDto();

        //when
        //then
        mockMvc.perform(post("/api/v1/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderRequestDto))
                )
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("id").doesNotExist())
                .andExpect(jsonPath("code").value("BAD"))
        ;
    }

    @Test
    @DisplayName("주문 취소 테스트")
    void 주문_취소_테스트() throws Exception {
        //given
        // Member 생성
        Member member1 = createMember("jung", "서울", "123", "456");
        Member savedMember = memberRepository.save(member1);

        // Item 생성
        Book book1 = createBook("코딩", 10_000, 10, "1234", "위키북스");
        Book savedItem = itemRepository.save(book1);

        // 배송 정보 생성
        Delivery delivery = Delivery.createDelivery(savedMember.getAddress(), DeliveryStatus.READY);

        // 상품 주문 생성
        OrderItem orderItem = OrderItem.createOrderItem(savedItem, savedItem.getPrice(), 1);

        // 주문 생성
        Order order = Order.createOrder(savedMember, delivery, orderItem);
        Order savedOrder = orderRepository.save(order);

        //then
        mockMvc.perform(post("/api/v1/" + savedOrder.getId() + " /cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(print());

        Order cancelOrder = orderRepository.findById(savedOrder.getId()).orElseThrow(() -> new IllegalStateException("주문을 찾을 수 없습니다."));
        Assertions.assertEquals(cancelOrder.getStatus(), OrderStatus.CANCEL);
    }

    @Test
    @DisplayName("주문 조건 검색 테스트 - 이름으로 검색")
    void 주문_조건_검색_테스트() throws Exception {
        //given

        // Member 생성
        Member member1 = createMember("jung", "서울", "123", "456");
        Member member2 = createMember("ho", "경기", "456", "789");
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);

        // Item 생성
        Book book1 = createBook("코딩", 10_000, 10, "1234", "위키북스");
        Book book2 = createBook("테스트", 20_000, 20, "1234", "한빛미디어");
        Book savedItem1 = itemRepository.save(book1);
        Book savedItem2 = itemRepository.save(book2);

        // 배송 정보 생성
        Delivery delivery1 = Delivery.createDelivery(savedMember1.getAddress(), DeliveryStatus.READY);
        Delivery delivery2 = Delivery.createDelivery(savedMember2.getAddress(), DeliveryStatus.READY);

        // 상품 주문 생성
        OrderItem orderItem1 = OrderItem.createOrderItem(savedItem1, savedItem1.getPrice(), 1);
        OrderItem orderItem2 = OrderItem.createOrderItem(savedItem2, savedItem2.getPrice(), 1);

        // 주문 생성
        Order order1 = Order.createOrder(savedMember1, delivery1, orderItem1);
        Order order2 = Order.createOrder(savedMember2, delivery2, orderItem2);

        // 주문 저장
        orderRepository.save(order1);
        orderRepository.save(order2);

        // 주문 조건 설정
        OrderSearch orderSearch = new OrderSearch();
        // 주문자 이름 jung
        orderSearch.setMemberName("jung");

        //then
        mockMvc.perform(post("/api/v1/orders/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(orderSearch))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").exists())
                .andExpect(jsonPath("$[0].member.name").value("jung"))
                .andDo(print());
    }


    @Test
    @DisplayName("주문 조건 검색 테스트 - 주문 상태로 검색")
    void 주문_조건_검색_테스트_주문_상태() throws Exception {
        //given

        // Member 생성
        Member member1 = createMember("jung", "서울", "123", "456");
        Member member2 = createMember("ho", "경기", "456", "789");
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);

        // Item 생성
        Book book1 = createBook("코딩", 10_000, 10, "1234", "위키북스");
        Book book2 = createBook("테스트", 20_000, 20, "1234", "한빛미디어");
        Book savedItem1 = itemRepository.save(book1);
        Book savedItem2 = itemRepository.save(book2);

        // 배송 정보 생성
        Delivery delivery1 = Delivery.createDelivery(savedMember1.getAddress(), DeliveryStatus.READY);
        Delivery delivery2 = Delivery.createDelivery(savedMember2.getAddress(), DeliveryStatus.READY);

        // 상품 주문 생성
        OrderItem orderItem1 = OrderItem.createOrderItem(savedItem1, savedItem1.getPrice(), 1);
        OrderItem orderItem2 = OrderItem.createOrderItem(savedItem2, savedItem2.getPrice(), 1);

        // 주문 생성
        Order order1 = Order.createOrder(savedMember1, delivery1, orderItem1);
        // 주문 취소
        order1.cancel();

        Order order2 = Order.createOrder(savedMember2, delivery2, orderItem2);

        // 주문 저장
        orderRepository.save(order1);
        orderRepository.save(order2);

        // 주문 조건 설정
        OrderSearch orderSearch = new OrderSearch();
        // 주문자 이름 jung
        orderSearch.setOrderStatus(OrderStatus.CANCEL);

        //then
        mockMvc.perform(post("/api/v1/orders/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(orderSearch))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").exists())
                .andExpect(jsonPath("$[0].member.name").value("jung"))
                .andDo(print());
    }

    private CreateOrderRequestDto getCreateOrderRequestDto(Member savedMember, Book savedItem) {
        CreateOrderRequestDto createOrderRequestDto = new CreateOrderRequestDto();

        createOrderRequestDto.setItemId(savedItem.getId());
        createOrderRequestDto.setMemberId(savedMember.getId());
        createOrderRequestDto.setCount(1);
        createOrderRequestDto.setAddressZipcode("123");
        createOrderRequestDto.setAddressStreet("456");
        createOrderRequestDto.setAddressCity("경기도");
        return createOrderRequestDto;
    }


    private Member createMember(String name, String city, String street, String zipcode) {
        Member member = new Member();
        member.setAddress(new Address(city, street, zipcode));
        member.setName(name);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity, String isbn, String author) {
        Book item = new Book();
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
        item.setIsbn(isbn);
        item.setAuthor(author);
        return item;
    }
}