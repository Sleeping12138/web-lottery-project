package com.test.lotterysystem.Controller;

import com.test.lotterysystem.Comment.ErrorCode.ControllerErrorCode;
import com.test.lotterysystem.Comment.Exception.ControllerException;
import com.test.lotterysystem.Comment.Utils.MyBeanUtil;
import com.test.lotterysystem.Controller.Param.ActivityCreateParam;
import com.test.lotterysystem.Controller.Param.PageParam;
import com.test.lotterysystem.Controller.Result.ActivityCreateResult;
import com.test.lotterysystem.Controller.Result.ActivityDetailResult;
import com.test.lotterysystem.Controller.Result.GetActivityResult;
import com.test.lotterysystem.Service.ActivityService;
import com.test.lotterysystem.Service.DTO.ActivityCreateDTO;
import com.test.lotterysystem.Service.DTO.ActivityDetailDTO;
import com.test.lotterysystem.Service.DTO.ActivityListResultDTO;
import com.test.lotterysystem.Service.DTO.ActivityResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RequestMapping("/activity")
@RestController
@Slf4j
public class ActivityController {
    @Autowired
    private ActivityService activityService;


    /**
     * 创建活动
     * @param activityCreateParam
     * @return
     */
    @RequestMapping("/create")
    public ActivityCreateResult create(@Validated @RequestBody ActivityCreateParam activityCreateParam) {
        log.info("activityCreateParam为------:{}", activityCreateParam);
        ActivityCreateDTO activityCreateDTO = activityService.create(activityCreateParam);

        return MyBeanUtil.transOfActivityCreateResult(activityCreateDTO);
    }

    /**
     * 获取活动列表
     * @param pageParam
     * @return
     */
    @RequestMapping("/getActivityList")
    public GetActivityResult getActivityList(PageParam pageParam){
        log.info("pageParam为:{}",pageParam);
        ActivityListResultDTO<ActivityResultDTO> activityList = activityService.getActivityList(pageParam);
        return MyBeanUtil.transOfGetActivityResult(activityList);
    }

    @RequestMapping("/getActivityDetail")
    public ActivityDetailResult getActivityDetail(Long activityId){
        log.info("activityId为：-----{}",activityId);
        ActivityDetailDTO activityDetailDTO = activityService.getActivityDetail(activityId);
        return MyBeanUtil.transOfActivityDetailResult(activityDetailDTO);
    }

}
