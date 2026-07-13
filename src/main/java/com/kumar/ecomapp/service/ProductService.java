package com.kumar.ecomapp.service;

import com.kumar.ecomapp.dto.ProductRequestDTO;
import com.kumar.ecomapp.dto.ProductResponseDTO;
import com.kumar.ecomapp.dto.UpdateProductDTO;
import org.springframework.data.domain.Page;

public interface ProductService {

    // Create Product
    ProductResponseDTO createProduct(
            ProductRequestDTO requestDTO
    );

    // Get Product By Id
    ProductResponseDTO getProductById(
            Long productId
    );

    // Get All Products (Pagination)
    Page<ProductResponseDTO> getAllProducts(
            int page,
            int size,
            String sortBy,
            String direction
    );

    // Update Product
    ProductResponseDTO updateProduct(
            Long productId,
            UpdateProductDTO updateDTO
    );

    // Delete Product
    void deleteProduct(
            Long productId
    );

}