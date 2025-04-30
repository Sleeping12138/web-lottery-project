package com.test.lotterysystem.Service.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PrizeDTO {
    private Long prizeId;

    private String prizeName;

    private String description;

    private BigDecimal price;

    private String imageUrl;
}
