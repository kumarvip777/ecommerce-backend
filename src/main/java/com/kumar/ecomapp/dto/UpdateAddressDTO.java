package com.kumar.ecomapp.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAddressDTO {

    @Size(max = 20, message = "Door number cannot exceed 20 characters")
    private String doorNo;

    @Size(max = 100, message = "Street cannot exceed 100 characters")
    private String street;

    @Size(max = 50, message = "City cannot exceed 50 characters")
    private String city;

    @Size(max = 50, message = "State cannot exceed 50 characters")
    private String state;

    @Size(min = 6, max = 10,
            message = "Postal code must be between 6 and 10 characters")
    private String postalCode;

    @Size(max = 50, message = "Country cannot exceed 50 characters")
    private String country;

    public boolean isEmpty() {
        return doorNo == null
                && street == null
                && city == null
                && state == null
                && postalCode == null
                && country == null;
    }
}