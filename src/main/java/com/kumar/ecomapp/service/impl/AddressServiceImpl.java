package com.kumar.ecomapp.service.impl;

import com.kumar.ecomapp.dto.AddressRequestDTO;
import com.kumar.ecomapp.dto.AddressResponseDTO;
import com.kumar.ecomapp.dto.UpdateAddressDTO;
import com.kumar.ecomapp.entity.Address;
import com.kumar.ecomapp.entity.User;
import com.kumar.ecomapp.exception.address.AddressLimitExceededException;
import com.kumar.ecomapp.exception.address.AddressNotFoundException;
import com.kumar.ecomapp.exception.address.NoAddressesFoundException;
import com.kumar.ecomapp.exception.user.UserNotFoundException;
import com.kumar.ecomapp.repo.AddressRepository;
import com.kumar.ecomapp.repo.UserRepository;
import com.kumar.ecomapp.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private static final Logger logger =
            LoggerFactory.getLogger(AddressServiceImpl.class);

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public AddressResponseDTO createAddress(
            Long userId,
            @Valid AddressRequestDTO requestDTO) {

        logger.info("Starting address creation for userId: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + userId
                        ));

        List<Address> addresses =
                addressRepository.findByUserUserId(userId);

        if (addresses.size() >= 2) {
            throw new AddressLimitExceededException(
                    "A user can have a maximum of 2 addresses"
            );
        }

        Address address =
                modelMapper.map(requestDTO, Address.class);

        address.setUser(user);

        Address savedAddress =
                addressRepository.save(address);

        logger.info(
                "Address created successfully. addressId={}, userId={}",
                savedAddress.getAddressId(),
                userId
        );

        AddressResponseDTO responseDTO =
                modelMapper.map(
                        savedAddress,
                        AddressResponseDTO.class
                );

        responseDTO.setUserId(
                savedAddress.getUser().getUserId()
        );

        return responseDTO;
    }

    @Override
    public AddressResponseDTO getAddressById(Long addressId) {

        logger.info(
                "Fetching address with id: {}",
                addressId
        );

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() ->
                        new AddressNotFoundException(
                                "Address not found with id: " + addressId
                        ));

        AddressResponseDTO responseDTO =
                modelMapper.map(
                        address,
                        AddressResponseDTO.class
                );

        responseDTO.setUserId(
                address.getUser().getUserId()
        );

        return responseDTO;
    }

    @Override
    public List<AddressResponseDTO> getAddressesByUserId(
            Long userId) {

        logger.info(
                "Fetching addresses for userId: {}",
                userId
        );

        List<Address> addresses =
                addressRepository.findByUserUserId(userId);

        if (addresses.isEmpty()) {
            throw new NoAddressesFoundException(
                    "No addresses found for user id: " + userId
            );
        }

        return addresses.stream()
                .map(address -> {

                    AddressResponseDTO dto =
                            modelMapper.map(
                                    address,
                                    AddressResponseDTO.class
                            );

                    dto.setUserId(
                            address.getUser().getUserId()
                    );

                    return dto;
                })
                .toList();
    }

    @Override
    @Transactional
    public AddressResponseDTO updateAddress(
            Long addressId,
            @Valid UpdateAddressDTO updateDTO) {

        logger.info(
                "Updating address with id: {}",
                addressId
        );

        if (updateDTO.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one field must be provided for update"
            );
        }

        Address address =
                addressRepository.findById(addressId)
                        .orElseThrow(() ->
                                new AddressNotFoundException(
                                        "Address not found with id: "
                                                + addressId
                                ));

        if (updateDTO.getDoorNo() != null) {
            address.setDoorNo(
                    updateDTO.getDoorNo().trim()
            );
        }

        if (updateDTO.getStreet() != null) {
            address.setStreet(
                    updateDTO.getStreet().trim()
            );
        }

        if (updateDTO.getCity() != null) {
            address.setCity(
                    updateDTO.getCity().trim()
            );
        }

        if (updateDTO.getState() != null) {
            address.setState(
                    updateDTO.getState().trim()
            );
        }

        if (updateDTO.getPostalCode() != null) {
            address.setPostalCode(
                    updateDTO.getPostalCode().trim()
            );
        }

        if (updateDTO.getCountry() != null) {
            address.setCountry(
                    updateDTO.getCountry().trim()
            );
        }

        Address updatedAddress =
                addressRepository.save(address);

        logger.info(
                "Address updated successfully. addressId={}",
                addressId
        );

        AddressResponseDTO responseDTO =
                modelMapper.map(
                        updatedAddress,
                        AddressResponseDTO.class
                );

        responseDTO.setUserId(
                updatedAddress.getUser().getUserId()
        );

        return responseDTO;
    }

    @Override
    @Transactional
    public void deleteAddress(Long addressId) {

        logger.info(
                "Deleting address with id: {}",
                addressId
        );

        Address address =
                addressRepository.findById(addressId)
                        .orElseThrow(() ->
                                new AddressNotFoundException(
                                        "Address not found with id: "
                                                + addressId
                                ));

        addressRepository.delete(address);

        logger.info(
                "Address deleted successfully. addressId={}",
                addressId
        );
    }
    private void applyDoorNoUpdate(
            Address address,
            UpdateAddressDTO updateDTO) {

        if (updateDTO.getDoorNo() == null
                || updateDTO.getDoorNo().isBlank()) {
            return;
        }

        address.setDoorNo(updateDTO.getDoorNo().trim());

        logger.info("Door number updated");
    }
    private void applyStreetUpdate(
            Address address,
            UpdateAddressDTO updateDTO) {

        if (updateDTO.getStreet() == null
                || updateDTO.getStreet().isBlank()) {
            return;
        }

        address.setStreet(updateDTO.getStreet().trim());

        logger.info("Street updated");
    }
    private void applyCityUpdate(
            Address address,
            UpdateAddressDTO updateDTO) {

        if (updateDTO.getCity() == null
                || updateDTO.getCity().isBlank()) {
            return;
        }

        address.setCity(updateDTO.getCity().trim());

        logger.info("City updated");
    }
    private void applyStateUpdate(
            Address address,
            UpdateAddressDTO updateDTO) {

        if (updateDTO.getState() == null
                || updateDTO.getState().isBlank()) {
            return;
        }

        address.setState(updateDTO.getState().trim());

        logger.info("State updated");
    }
    private void applyPostalCodeUpdate(
            Address address,
            UpdateAddressDTO updateDTO) {

        if (updateDTO.getPostalCode() == null
                || updateDTO.getPostalCode().isBlank()) {
            return;
        }

        address.setPostalCode(updateDTO.getPostalCode().trim());

        logger.info("Postal code updated");
    }
    private void applyCountryUpdate(
            Address address,
            UpdateAddressDTO updateDTO) {

        if (updateDTO.getCountry() == null
                || updateDTO.getCountry().isBlank()) {
            return;
        }

        address.setCountry(updateDTO.getCountry().trim());

        logger.info("Country updated");
    }
}