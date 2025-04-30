package com.test.lotterysystem.Dao.DaoObject;

import com.test.lotterysystem.Service.Enum.ActivityUserEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ActivityUserDO extends BaseDO{
    private Long activityId;

    private Long userId;

    private String userName;

    private ActivityUserEnum status;
}
