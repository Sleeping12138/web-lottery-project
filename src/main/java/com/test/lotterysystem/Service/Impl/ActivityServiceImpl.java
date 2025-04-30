package com.test.lotterysystem.Service.Impl;

import com.test.lotterysystem.Comment.ErrorCode.ServiceErrorCode;
import com.test.lotterysystem.Comment.Exception.ServiceException;
import com.test.lotterysystem.Comment.Utils.JacksonUtil;
import com.test.lotterysystem.Comment.Utils.RedisUtil;
import com.test.lotterysystem.Controller.Param.ActivityCreateParam;
import com.test.lotterysystem.Controller.Param.PageParam;
import com.test.lotterysystem.Controller.Result.ActivityCreatePrize;
import com.test.lotterysystem.Controller.Result.ActivityCreateUser;
import com.test.lotterysystem.Dao.DaoObject.*;
import com.test.lotterysystem.Dao.Mapper.*;
import com.test.lotterysystem.Service.ActivityService;
import com.test.lotterysystem.Service.DTO.ActivityCreateDTO;
import com.test.lotterysystem.Service.DTO.ActivityDetailDTO;
import com.test.lotterysystem.Service.DTO.ActivityListResultDTO;
import com.test.lotterysystem.Service.DTO.ActivityResultDTO;
import com.test.lotterysystem.Service.Enum.ActivityEnum;
import com.test.lotterysystem.Service.Enum.ActivityPrizeEnum;
import com.test.lotterysystem.Service.Enum.ActivityPrizeTiersEnum;
import com.test.lotterysystem.Service.Enum.ActivityUserEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ActivityServiceImpl implements ActivityService {

    private final String ACTIVITY_PREFIX = "ACTIVITY_";

    private final Long ACTIVITY_TIMEOUT = 60 * 60 * 24 * 5L;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PrizeMapper prizeMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private ActivityPrizeMapper activityPrizeMapper;

    @Autowired
    private ActivityUserMapper activityUserMapper;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 创建活动
     *
     * @param activityCreateParam
     * @return
     */
    @Override
    public ActivityCreateDTO create(ActivityCreateParam activityCreateParam) {
        // 对活动中的数据进行校验
        checkActivityInfo(activityCreateParam);

        // 将活动信息存储进数据库
        ActivityDO activityDO = new ActivityDO();
        activityDO.setActivityName(activityCreateParam.getActivityName());
        activityDO.setDescription(activityCreateParam.getDescription());
        activityDO.setStatus(ActivityEnum.PROCEED.name());

        activityMapper.insert(activityDO);


        // 将活动对应奖品存储入库
        List<ActivityCreatePrize> prizeList = activityCreateParam.getActivityPrizeList();
        List<ActivityPrizeDO> activityPrizeDOS = prizeList.stream().map(prize -> {
            ActivityPrizeDO activityPrizeDO = new ActivityPrizeDO();
            activityPrizeDO.setActivityId(activityDO.getId());
            activityPrizeDO.setPrizeId(prize.getPrizeId());
            activityPrizeDO.setPrizeAmount(prize.getPrizeAmount());
            activityPrizeDO.setPrizeTiers(prize.getPrizeTiers());
            activityPrizeDO.setStatus(ActivityPrizeEnum.INIT.name());
            return activityPrizeDO;
        }).collect(Collectors.toList());

        activityPrizeMapper.batchInsertActivityPrize(activityPrizeDOS);


        // 将活动对应人员存储入库
        List<ActivityCreateUser> userList = activityCreateParam.getActivityUserList();
        List<ActivityUserDO> userDOS = userList.stream().map(user -> {
            ActivityUserDO activityUserDO = new ActivityUserDO();
            activityUserDO.setActivityId(activityDO.getId());
            activityUserDO.setUserId(user.getUserId());
            activityUserDO.setUserName(user.getUserName());
            activityUserDO.setStatus(ActivityUserEnum.INIT);
            return activityUserDO;
        }).collect(Collectors.toList());

        activityUserMapper.batchInsertActivityUser(userDOS);

        // 将整合数据存储到redis中
        // 创建整理类
        List<Long> prizeIds = activityPrizeDOS.stream().map(prize -> prize.getPrizeId()).collect(Collectors.toList());
        List<PrizeDO> selectPrizeInfo = prizeMapper.getPrizeByIds(prizeIds);

        // 封装整合数据到一个对象中
        ActivityDetailDTO activityDetailDTO = createActivityDetailDTO(activityDO, activityPrizeDOS, userDOS, selectPrizeInfo);

        // 存储数据到redis中
        saveSourceToRedis(activityDetailDTO);

        // 返回数据
        ActivityCreateDTO activityCreateDTO = new ActivityCreateDTO();
        activityCreateDTO.setActivityId(activityDO.getId());
        return activityCreateDTO;
    }

    /**
     * 存储数据到redis中
     *
     * @param activityDetailDTO
     */
    private void saveSourceToRedis(ActivityDetailDTO activityDetailDTO) {
        if (activityDetailDTO == null || activityDetailDTO.getActivityId() == null) {
            log.error("整合的活动数据为空");
            return;
        }

        try {
            redisUtil.set(ACTIVITY_PREFIX + activityDetailDTO.getActivityId()
                    , JacksonUtil.writeValueAsString(activityDetailDTO), ACTIVITY_TIMEOUT);
        } catch (Exception e) {
            log.error("缓存数据发生异常！！！");
        }
    }

    /**
     * 获取redis中的数据
     */
    public ActivityDetailDTO getActivityDetailDTO(Long activityId) {
        if (activityId == null) {
            log.error("传入的activityId为空");
        }

        try {
            String str = redisUtil.get(ACTIVITY_PREFIX + activityId);
            if (!StringUtils.hasText(str)) {
                log.error("获取的缓存活动数据为空,activityId==" + activityId);
                return null;
            }

            return JacksonUtil.readValue(str, ActivityDetailDTO.class);
        } catch (Exception e) {
            log.error("获取的缓存活动数据异常,activityId==,-----" + activityId, e);
            return null;
        }
    }


    /**
     * 封装整合数据
     *
     * @param activityDO
     * @param activityPrizeDOS
     * @param userDOS
     * @param selectPrizeInfo
     * @return
     */
    private ActivityDetailDTO createActivityDetailDTO(ActivityDO activityDO
            , List<ActivityPrizeDO> activityPrizeDOS
            , List<ActivityUserDO> userDOS
            , List<PrizeDO> selectPrizeInfo) {
        ActivityDetailDTO activityDetailDTO = new ActivityDetailDTO();
        activityDetailDTO.setActivityId(activityDO.getId());
        activityDetailDTO.setActivityName(activityDO.getActivityName());
        activityDetailDTO.setDesc(activityDO.getDescription());
        activityDetailDTO.setStatus(ActivityEnum.forName(activityDO.getStatus()));

        List<ActivityDetailDTO.PrizeDetailDTO> prizeDTOListResult = activityPrizeDOS.stream().map(prize -> {
            ActivityDetailDTO.PrizeDetailDTO detailDTO = new ActivityDetailDTO.PrizeDetailDTO();
            detailDTO.setPrizeId(prize.getPrizeId());

            Optional<PrizeDO> optionalPrizeDO = selectPrizeInfo.stream()
                    .filter(prizeDO -> prizeDO.getId().equals(prize.getPrizeId())).findFirst();

            // 从众多奖品中选取出当前活动需要的奖品
            optionalPrizeDO.ifPresent(prizeDO -> {
                detailDTO.setPrizeName(prizeDO.getName());
                detailDTO.setDescription(prizeDO.getDescription());
                detailDTO.setPrice(prizeDO.getPrice());
                detailDTO.setImageUrl(prizeDO.getImageUrl());
            });

            detailDTO.setTiers(ActivityPrizeTiersEnum.forName(prize.getPrizeTiers()));
            detailDTO.setPrizeAmount(prize.getPrizeAmount());
            detailDTO.setStatus(ActivityPrizeEnum.forName(prize.getStatus()));

            return detailDTO;
        }).collect(Collectors.toList());

        activityDetailDTO.setPrizeDTOList(prizeDTOListResult);

        List<ActivityDetailDTO.UserDetailDTO> userDTOListResult = userDOS.stream().map(user -> {
            ActivityDetailDTO.UserDetailDTO detailDTO = new ActivityDetailDTO.UserDetailDTO();
            detailDTO.setUserId(user.getUserId());
            detailDTO.setUserName(user.getUserName());
            detailDTO.setStatus(user.getStatus());
            return detailDTO;
        }).collect(Collectors.toList());

        activityDetailDTO.setUserDTOList(userDTOListResult);

        log.info("新创建的activityDetailDTO为：{}", activityDetailDTO);

        return activityDetailDTO;
    }


    /**
     * 校验创建活动时的数据
     *
     * @param activityCreateParam
     */
    private void checkActivityInfo(ActivityCreateParam activityCreateParam) {
        // 活动数据不能为空
        if (activityCreateParam == null) {
            throw new ServiceException(ServiceErrorCode.ACTIVITY_INFO_IS_EMPTY);
        }

        // 校验人员身份
        List<ActivityCreateUser> userList = activityCreateParam.getActivityUserList();
        List<Long> userIds = userList.stream()
                .map(user -> user.getUserId()).distinct().collect(Collectors.toList());

        List<Long> usersResult = userMapper.selectUserByIds(userIds);

        if (usersResult == null) {
            throw new ServiceException(ServiceErrorCode.ACTIVITY_USERS_IS_ERROR);
        }

        for (Long userId : userIds) {
            if (!usersResult.contains(userId)) {
                throw new ServiceException(ServiceErrorCode.ACTIVITY_USERS_IS_ERROR);
            }
        }

        // 校验奖品
        List<ActivityCreatePrize> prizeList = activityCreateParam.getActivityPrizeList();
        List<Long> prizesIds = prizeList.stream()
                .map(prize -> prize.getPrizeId()).collect(Collectors.toList());

        List<Long> prizesResult = prizeMapper.selectPrizeByIds(prizesIds);

        if (prizesResult == null) {
            throw new ServiceException(ServiceErrorCode.ACTIVITY_PRIZES_IS_ERROR);
        }

        for (Long prizeId : prizesIds) {
            if (!prizesResult.contains(prizeId)) {
                throw new ServiceException(ServiceErrorCode.ACTIVITY_PRIZES_IS_ERROR);
            }
        }

        // 校验人员数目大于奖品数目
        long userNumber = usersResult.size();
        long prizeNumber = prizeList.stream().mapToLong(prize -> prize.getPrizeAmount()).sum();

        if (prizeNumber > userNumber) {
            throw new ServiceException(ServiceErrorCode.ACTIVITY_PRIZE_NUMBER_ERROR);
        }
    }

    /**
     * 获取活动列表
     *
     * @param pageParam
     * @return
     */
    @Override
    public ActivityListResultDTO<ActivityResultDTO> getActivityList(PageParam pageParam) {
        ActivityListResultDTO<ActivityResultDTO> resultDTO = new ActivityListResultDTO<>();
        Integer count = activityMapper.count();
        resultDTO.setTotal(count);

        List<ActivityListDO> activityList = activityMapper
                .getActivityList(pageParam.offset(pageParam.getCurrentPage(), pageParam.getPageSize())
                        , pageParam.getPageSize());

        List<ActivityResultDTO> dtoList = activityList.stream().map(activityListDO -> {
            ActivityResultDTO activityResultDTO = new ActivityResultDTO();
            activityResultDTO.setActivityId(activityListDO.getId());
            activityResultDTO.setActivityName(activityListDO.getActivityName());
            activityResultDTO.setDescription(activityListDO.getDescription());
            activityResultDTO.setStatus(ActivityEnum.forName(activityListDO.getStatus()));
            return activityResultDTO;
        }).collect(Collectors.toList());

        resultDTO.setRecords(dtoList);

        return resultDTO;
    }

    @Override
    public ActivityDetailDTO getActivityDetail(Long activityId) {
        if (activityId == null) {
            log.error("activityId为空");
            return null;
        }

        // 先从redis中进行获取
        ActivityDetailDTO activityDetailDTO = getActivityDetailDTO(activityId);
        if (activityDetailDTO != null) {
            return activityDetailDTO;
        }

        // redis中没有结果，从数据库中进行获取
        ActivityDO activityDO = activityMapper.getActivityDetail(activityId);

        List<ActivityPrizeDO> activityPrizeDO = activityPrizeMapper.getActivityPrizeDetail(activityId);

        List<ActivityUserDO> activityUserDOS = activityUserMapper.getActivityUserDetail(activityId);

        List<Long> prizeIds = activityPrizeDO.stream()
                .map(prize -> prize.getPrizeId()).collect(Collectors.toList());

        List<PrizeDO> prizeDOS = prizeMapper.getPrizeByIds(prizeIds);

        ActivityDetailDTO detailDTO = createActivityDetailDTO(activityDO, activityPrizeDO, activityUserDOS, prizeDOS);

        saveSourceToRedis(detailDTO);

        return detailDTO;
    }

    @Override
    public void cacheActivity(Long activityId) {
        if (activityId == null) {
            log.error("缓存的活动id为空");
            throw new ServiceException(ServiceErrorCode.CACHE_ACTIVITY_ID_IS_EMPTY);
        }

        ActivityDO activityDO = activityMapper.getActivityDetail(activityId);

        List<ActivityPrizeDO> activityPrizeDO = activityPrizeMapper.getActivityPrizeDetail(activityId);

        List<ActivityUserDO> activityUserDOS = activityUserMapper.getActivityUserDetail(activityId);

        List<Long> prizeIds = activityPrizeDO.stream()
                .map(prize -> prize.getPrizeId()).collect(Collectors.toList());

        List<PrizeDO> prizeDOS = prizeMapper.getPrizeByIds(prizeIds);

        ActivityDetailDTO detailDTO = createActivityDetailDTO(activityDO, activityPrizeDO, activityUserDOS, prizeDOS);

        saveSourceToRedis(detailDTO);
    }
}
