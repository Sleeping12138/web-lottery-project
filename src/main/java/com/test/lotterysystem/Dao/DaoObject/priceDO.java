package com.test.lotterysystem.Dao.DaoObject;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class priceDO extends BaseDO{
    private String prizeName;

    private String description;

    private String imageUrl;

    private BigDecimal price;

}
