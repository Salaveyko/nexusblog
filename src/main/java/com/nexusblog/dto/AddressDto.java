package com.nexusblog.dto;

import com.google.common.base.Strings;
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

    public boolean isEmpty(){
        return Strings.isNullOrEmpty(country)
                && Strings.isNullOrEmpty(statement)
                && Strings.isNullOrEmpty(postalCode)
                && buildingNumber == null;
    }
}
