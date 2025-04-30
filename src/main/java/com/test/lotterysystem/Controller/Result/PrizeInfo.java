package com.test.lotterysystem.Controller.Result;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PrizeInfo {
    private Long prizeId;

    private String prizeName;

    private String description;

    private BigDecimal price;

    private String imageUrl;

}
