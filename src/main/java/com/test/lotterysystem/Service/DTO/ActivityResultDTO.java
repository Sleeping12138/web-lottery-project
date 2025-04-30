package com.test.lotterysystem.Service.DTO;

import com.test.lotterysystem.Service.Enum.ActivityEnum;
import lombok.Data;

@Data
public class ActivityResultDTO {
    private Long activityId;

    private String activityName;

    private String description;

    private ActivityEnum status;

    public Boolean valid() {
        return status.equals(ActivityEnum.PROCEED);
    }
}
