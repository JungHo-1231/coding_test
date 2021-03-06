package com.jungho.coding_test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jungho.coding_test.domain.Address;
import com.jungho.coding_test.domain.Member;
import com.jungho.coding_test.domain.OrderStatus;
import com.jungho.coding_test.domain.item.Book;
import com.jungho.coding_test.dto.member.CreateMemberRequestDto;
import com.jungho.coding_test.dto.member.MemberDto;
import com.jungho.coding_test.dto.order.CreateOrderRequestDto;
import com.jungho.coding_test.dto.order.OrderSearch;
import com.jungho.coding_test.repository.ItemRepository;
import com.jungho.coding_test.repository.MemberRepository;
import com.jungho.coding_test.service.OrderService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    OrderService orderService;


    @Test
    @DisplayName("ํ์ ์กฐํ ")
    void findMembers() throws Exception {
        //given
        Member member1 = createMember("jung", "์์ธ", "123", "456");
        Member member2 = createMember("kim", "์์ธ", "123", "456");

        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<MemberDto> findMembers = memberRepository.findAllMemberUsingQueryDsl();

        //then
        Assertions.assertThat(findMembers).isNotNull();
        Assertions.assertThat(findMembers).extracting("name").contains("jung", "kim");
    }

    @Test
    @DisplayName("ํ์ ๋ฑ๋ก ")
    void ํ์_๋ฑ๋ก_ํ์คํธ() throws Exception {
        //given
        CreateMemberRequestDto memberRequestDto = createMemberRequestDto();

        //then
        mockMvc.perform(post("/api/v1/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequestDto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
        ;
    }

    @Test
    @DisplayName("ํ์ ๋ฑ๋ก ์คํจ - ์ด๋ฆ์ ๊ธฐ์ํ์ง ์์ ๊ฒฝ์ฐ")
    void ํ์_๋ฑ๋ก_์คํจ_ํ์คํธ() throws Exception {
        //given
        CreateMemberRequestDto memberRequestDto = new CreateMemberRequestDto();

        //then
        mockMvc.perform(post("/api/v1/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequestDto))
                )
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("id").doesNotExist())
                .andExpect(jsonPath("code").value("BAD"))
        ;
    }

    @Test
    @DisplayName("ํ์ ๋จ๊ฑด ์กฐํ")
    void ํ์_๋จ๊ฑด_์กฐํ() throws Exception {
        //given
        Member member1 = createMember("jung", "์์ธ", "123", "456");
        //when
        Member savedMember = memberRepository.save(member1);
        //then
        mockMvc.perform(get("/api/v1/member/" + savedMember.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").exists())
        ;
    }

    @Test
    @DisplayName("ํ์ ์?์ฒด ์กฐํ")
    void ํ์_์?์ฒด_์กฐํ() throws Exception {
        //given
        Member member1 = createMember("jung", "์์ธ", "123", "456");
        Member member2 = createMember("kim", "์์ธ", "123", "456");

        memberRepository.save(member1);
        memberRepository.save(member2);

        //then
        mockMvc.perform(get("/api/v1/members"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[*].id").exists())
                .andExpect(jsonPath("$[*].name").exists());
    }

    private CreateMemberRequestDto createMemberRequestDto() {
        CreateMemberRequestDto createMemberRequestDto = new CreateMemberRequestDto();
        createMemberRequestDto.setName("jung");
        createMemberRequestDto.setAddressCity("๊ฒฝ๊ธฐ");
        createMemberRequestDto.setAddressStreet("123");
        createMemberRequestDto.setAddressZipcode("456");

        return createMemberRequestDto;
    }

    private Member createMember(String name, String city, String street, String zipcode) {
        Member member = new Member();
        member.setAddress(new Address(city, street, zipcode));
        member.setName(name);
        return member;
    }

}