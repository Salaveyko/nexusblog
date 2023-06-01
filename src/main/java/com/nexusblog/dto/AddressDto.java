package com.nexusblog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private Long id;
    private String country;
    private String statement;
    private String street;
    private Long buildingNumber;
    private String postalCode;
}
