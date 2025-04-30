package com.test.lotterysystem.Controller.Result;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActivityCreatePrize {
    @NotNull(message = "活动关联的奖品id不能为空！")
    private Long prizeId;

    @NotNull(message = "奖品数量不能为空！")
    private Long prizeAmount;

    @NotBlank(message = "奖品等级不能为空！")
    private String prizeTiers;
}
