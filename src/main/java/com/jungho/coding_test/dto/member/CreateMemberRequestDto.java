package com.jungho.coding_test.dto.member;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreateMemberRequestDto {

    @NotEmpty
    private String name;

    private String addressCity;

    private String addressStreet;

    private String addressZipcode;
}
