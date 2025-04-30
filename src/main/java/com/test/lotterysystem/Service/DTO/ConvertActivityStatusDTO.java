package com.test.lotterysystem.Service.DTO;

import com.test.lotterysystem.Service.Enum.ActivityEnum;
import com.test.lotterysystem.Service.Enum.ActivityPrizeEnum;
import com.test.lotterysystem.Service.Enum.ActivityUserEnum;
import lombok.Data;

import java.util.List;

@Data
public class ConvertActivityStatusDTO {
    private Long activityId;

    private ActivityEnum targetActivityStatus;

    private Long prizeId;

    private ActivityPrizeEnum targetPrizeStatus;

    private List<Long> userIds;

    private ActivityUserEnum targetUserStatus;
}
