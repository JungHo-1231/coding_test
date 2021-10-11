package com.jungho.coding_test.dto.item;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateBookRequestDto {

    private Long Id;

    @NotEmpty
    private String name;
    @NotNull
    private int price;
    @NotNull
    private int stockQuantity;
    @NotNull
    private String author;
    @NotNull
    private String isbn;

}
