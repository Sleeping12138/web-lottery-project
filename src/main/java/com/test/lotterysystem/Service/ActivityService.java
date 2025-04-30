package com.test.lotterysystem.Service;

import com.test.lotterysystem.Controller.Param.ActivityCreateParam;
import com.test.lotterysystem.Controller.Param.PageParam;
import com.test.lotterysystem.Service.DTO.ActivityCreateDTO;
import com.test.lotterysystem.Service.DTO.ActivityDetailDTO;
import com.test.lotterysystem.Service.DTO.ActivityListResultDTO;
import com.test.lotterysystem.Service.DTO.ActivityResultDTO;

public interface ActivityService {
    ActivityCreateDTO create(ActivityCreateParam activityCreateParam);

    ActivityListResultDTO<ActivityResultDTO> getActivityList(PageParam pageParam);

    ActivityDetailDTO getActivityDetail(Long activityId);

    void cacheActivity(Long activityId);
}
