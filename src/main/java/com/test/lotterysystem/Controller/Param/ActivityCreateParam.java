package com.test.lotterysystem.Controller.Param;

import com.test.lotterysystem.Controller.Result.ActivityCreatePrize;
import com.test.lotterysystem.Controller.Result.ActivityCreateUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ActivityCreateParam implements Serializable {
    @NotBlank(message = "活动名称不能为空")
    private String activityName;

    @NotBlank(message = "活动描述不能为空！")
    private String description;

    @NotEmpty(message = "活动关联奖品列表不能为空！")
    @Valid
    private List<ActivityCreatePrize> activityPrizeList;

    @NotEmpty(message = "活动关联人员列表不能为空！")
    @Valid
    private List<ActivityCreateUser> activityUserList;

}
