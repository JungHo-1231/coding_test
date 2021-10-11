package com.jungho.coding_test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jungho.coding_test.domain.item.Book;
import com.jungho.coding_test.dto.item.CreateBookRequestDto;
import com.jungho.coding_test.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품 등록(book) 등록")
    void 상품_등록() throws Exception {
        //given
        CreateBookRequestDto requestDto = createBookRequestDto();
        //when

        //then
        mockMvc.perform(post("/api/v1/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestDto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists());
    }


    @Test
    @DisplayName("상품 등록 실패 - 상품 이름 미기재")
    void 상품_등록_실패_테스트() throws Exception {
        //given
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        //when

        //then
        mockMvc.perform(post("/api/v1/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestDto))
                )
                .andDo(print())
                .andExpect(jsonPath("id").doesNotExist())
                .andExpect(jsonPath("code").value("BAD"))
        ;
    }


    @Test
    @DisplayName("상품 단건 조회")
    void 상품_단건_조회() throws Exception {
        //given
        Book book = createBook("코딩", 10_000, 10, "1234", "위키북스");

        //when
        Book savedBook = itemRepository.save(book);

        //then
        mockMvc.perform(get("/api/v1/item/" + savedBook.getId())
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("price").exists())
                .andExpect(jsonPath("stockQuantity").exists())
                .andExpect(jsonPath("isbn").exists())
                .andExpect(jsonPath("author").exists())
        ;
    }


    @Test
    @DisplayName("상품 전체 조회")
    void 상품_전체_조회() throws Exception {

        //given
        Book book1 = createBook("코딩", 10_000, 10, "1234", "위키북스");
        Book book2 = createBook("테스트", 20_000, 20, "5678", "한빛미디어");

        //when
        itemRepository.save(book1);
        itemRepository.save(book2);

        //then
        mockMvc.perform(get("/api/v1/items")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[*].id").exists())
                .andExpect(jsonPath("$[*].name").exists())
                .andExpect(jsonPath("$[*].price").exists())
                .andExpect(jsonPath("$[*].stockQuantity").exists())
                .andExpect(jsonPath("$[*].isbn").exists())
                .andExpect(jsonPath("$[*].author").exists())
        ;
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


    private CreateBookRequestDto createBookRequestDto() {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setName("코딩테스트 완벽 대비");
        createBookRequestDto.setPrice(10_000);
        createBookRequestDto.setStockQuantity(10);
        createBookRequestDto.setIsbn("1234");
        createBookRequestDto.setAuthor("위키북스");

        return createBookRequestDto;
    }

}