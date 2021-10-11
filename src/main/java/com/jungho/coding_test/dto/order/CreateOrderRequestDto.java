package com.jungho.coding_test.dto.order;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateOrderRequestDto {

    @NotNull
    private Long memberId;

    @NotNull
    private Long itemId;

    @NotNull
    private int count;

    @NotEmpty
    private String addressCity;

    @NotEmpty
    private String addressStreet;

    @NotEmpty
    private String addressZipcode;

}
