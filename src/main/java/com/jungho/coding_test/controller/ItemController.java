package com.jungho.coding_test.controller;

import com.jungho.coding_test.dto.CreateResponseDto;
import com.jungho.coding_test.dto.item.CreateBookRequestDto;
import com.jungho.coding_test.dto.item.ItemDto;
import com.jungho.coding_test.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    /**
     * 상품 등록
     * (편의상 Item 등록은 Book 구현)
     *
     * @param createBookRequestDto
     * @return CreateResponseDto
     */
    @PostMapping("/api/v1/book")
    public CreateResponseDto createBook(@RequestBody @Validated CreateBookRequestDto createBookRequestDto) {
        Long saveId = itemService.saveItem(createBookRequestDto);
        return new CreateResponseDto(saveId);
    }

    /**
     * 전체 상품 조회
     *
     * @return List<ItemDto>
     */
    @GetMapping("/api/v1/items")
    public List<ItemDto> findMembers() {
        return itemService.findItems();
    }


    /**
     * 상품 단권 조회
     *
     * @param id
     * @return ItemDto
     */

    @GetMapping("/api/v1/item/{id}")
    public ItemDto findItem(@PathVariable("id") Long id) {
        return itemService.fineOne(id);
    }
}

