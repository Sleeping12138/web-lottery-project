package com.test.lotterysystem.Dao.DaoObject;

import com.test.lotterysystem.Service.Enum.ActivityPrizeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ActivityPrizeDO extends BaseDO {
    private Long activityId;

    private Long prizeId;

    private Long prizeAmount;

    private String prizeTiers;

    private String status;
}
