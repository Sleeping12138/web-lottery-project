package com.test.lotterysystem.Controller.Param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WinnerRecordParam {

    @NotNull(message = "活动id不能为空")
    private Long activityId;

    private Long prizeId;
}
