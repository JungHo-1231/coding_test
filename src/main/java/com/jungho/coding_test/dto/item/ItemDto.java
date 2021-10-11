package com.jungho.coding_test.dto.item;

import lombok.Data;

@Data
public class ItemDto {

    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;
}
