package com.kumar.ecomapp.dto;

import com.kumar.ecomapp.entity.enums.CategoryStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponseDTO {

    private Long categoryId;

    private String categoryName;

    private String description;

    private CategoryStatus status;
}