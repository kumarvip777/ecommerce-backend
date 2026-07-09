package com.kumar.ecomapp.controller;

import com.kumar.ecomapp.dto.AddressRequestDTO;
import com.kumar.ecomapp.dto.AddressResponseDTO;
import com.kumar.ecomapp.dto.UpdateAddressDTO;
import com.kumar.ecomapp.payload.ApiResponse;
import com.kumar.ecomapp.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(
        name = "Address Management",
        description = "APIs for managing user addresses"
)
public class AddressController {

    private static final Logger logger =
            LoggerFactory.getLogger(AddressController.class);

    private final AddressService addressService;

    @Operation(
            summary = "Create Address",
            description = "Creates a new address for a user"
    )
    @PostMapping("/users/{userId}/addresses")
    public ResponseEntity<ApiResponse<AddressResponseDTO>> createAddress(

            @PathVariable Long userId,

            @Valid
            @RequestBody AddressRequestDTO requestDTO,

            HttpServletRequest request) {

        logger.info(
                "Received request to create address for userId: {}",
                userId
        );

        AddressResponseDTO responseDTO =
                addressService.createAddress(
                        userId,
                        requestDTO
                );

        ApiResponse<AddressResponseDTO> response =
                ApiResponse.success(
                        responseDTO,
                        "Address created successfully",
                        request.getRequestURI()
                );

        logger.info(
                "Address created successfully for userId: {}",
                userId
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Get Address By Id",
            description = "Returns address details using address id"
    )
    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<ApiResponse<AddressResponseDTO>> getAddressById(

            @PathVariable Long addressId,

            HttpServletRequest request) {

        logger.info(
                "Received request to fetch addressId: {}",
                addressId
        );

        AddressResponseDTO responseDTO =
                addressService.getAddressById(addressId);

        ApiResponse<AddressResponseDTO> response =
                ApiResponse.success(
                        responseDTO,
                        "Address fetched successfully",
                        request.getRequestURI()
                );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get User Addresses",
            description = "Returns all addresses of a user"
    )
    @GetMapping("/users/{userId}/addresses")
    public ResponseEntity<ApiResponse<List<AddressResponseDTO>>> getAddressesByUserId(

            @PathVariable Long userId,

            HttpServletRequest request) {

        logger.info(
                "Received request to fetch addresses for userId: {}",
                userId
        );

        List<AddressResponseDTO> responseDTO =
                addressService.getAddressesByUserId(userId);

        ApiResponse<List<AddressResponseDTO>> response =
                ApiResponse.success(
                        responseDTO,
                        "Addresses fetched successfully",
                        request.getRequestURI()
                );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update Address",
            description = "Updates address details"
    )
    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<ApiResponse<AddressResponseDTO>> updateAddress(

            @PathVariable Long addressId,

            @Valid
            @RequestBody UpdateAddressDTO updateDTO,

            HttpServletRequest request) {

        logger.info(
                "Received request to update addressId: {}",
                addressId
        );

        AddressResponseDTO responseDTO =
                addressService.updateAddress(
                        addressId,
                        updateDTO
                );

        ApiResponse<AddressResponseDTO> response =
                ApiResponse.success(
                        responseDTO,
                        "Address updated successfully",
                        request.getRequestURI()
                );

        logger.info(
                "Address updated successfully. addressId: {}",
                addressId
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete Address",
            description = "Deletes address by address id"
    )
    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<ApiResponse<String>> deleteAddress(

            @PathVariable Long addressId,

            HttpServletRequest request) {

        logger.info(
                "Received request to delete addressId: {}",
                addressId
        );

        addressService.deleteAddress(addressId);

        ApiResponse<String> response =
                ApiResponse.success(
                        "Address deleted successfully",
                        "Address deleted successfully",
                        request.getRequestURI()
                );

        logger.info(
                "Address deleted successfully. addressId: {}",
                addressId
        );

        return ResponseEntity.ok(response);
    }
}