package com.kumar.ecomapp.service;

import com.kumar.ecomapp.dto.AddressRequestDTO;
import com.kumar.ecomapp.dto.AddressResponseDTO;
import com.kumar.ecomapp.dto.UpdateAddressDTO;

import java.util.List;

public interface AddressService {

    // Create Address
    AddressResponseDTO createAddress(
            Long userId,
            AddressRequestDTO requestDTO
    );

    // Get Address By Id
    AddressResponseDTO getAddressById(
            Long addressId
    );

    // Get All Addresses Of User
    List<AddressResponseDTO> getAddressesByUserId(
            Long userId
    );

    // Update Address
    AddressResponseDTO updateAddress(
            Long addressId,
            UpdateAddressDTO updateDTO
    );

    // Delete Address
    void deleteAddress(
            Long addressId
    );

}