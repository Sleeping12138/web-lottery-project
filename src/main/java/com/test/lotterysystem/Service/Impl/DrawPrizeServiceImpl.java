package com.test.lotterysystem.Service.Impl;

import com.test.lotterysystem.Comment.Constants.Constants;
import com.test.lotterysystem.Comment.ErrorCode.ServiceErrorCode;
import com.test.lotterysystem.Comment.Exception.ServiceException;
import com.test.lotterysystem.Comment.Utils.JacksonUtil;
import com.test.lotterysystem.Comment.Utils.MyBeanUtil;
import com.test.lotterysystem.Comment.Utils.RedisUtil;
import com.test.lotterysystem.Controller.Param.DrawPrizeParam;
import com.test.lotterysystem.Controller.Param.WinnerRecordParam;
import com.test.lotterysystem.Dao.DaoObject.*;
import com.test.lotterysystem.Dao.Mapper.*;
import com.test.lotterysystem.Service.DTO.WinnerRecordInfoDTO;
import com.test.lotterysystem.Service.DrawPrizeService;
import com.test.lotterysystem.Service.Enum.ActivityEnum;
import com.test.lotterysystem.Service.Enum.ActivityPrizeEnum;
import com.test.lotterysystem.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DrawPrizeServiceImpl implements DrawPrizeService {

    public static final String WINNING_RECORDS_PREFIX = "WINNING_RECORDS_";
    public static final Long WINNING_RECORDS_TIMEOUT = 60 * 60 * 24 * 2L;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private ActivityPrizeMapper activityPrizeMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PrizeMapper prizeMapper;

    @Autowired
    private WinnerRecordMapper winnerRecordMapper;

    @Autowired
    private RedisUtil redisUtil;


    /**
     * 发送数据到rabbitMQ中
     *
     * @param drawPrizeParam
     */
    @Override
    public void saveDrawPrizeInfo(DrawPrizeParam drawPrizeParam) {
        if (drawPrizeParam == null) {
            throw new ServiceException(ServiceErrorCode.DRAW_PRIZE_INFO_IS_EMPTY);
        }
        Map<String, String> map = new HashMap<>();
        map.put("messageId", UUID.randomUUID().toString().replace("-", ""));
        map.put("message", JacksonUtil.writeValueAsString(drawPrizeParam));

        // 发送数据到rabbitMQ中
        rabbitTemplate.convertAndSend(Constants.EXCHANGE_NAME, Constants.KEY, map);
        log.info("信息传递成功...{}", map);
    }

    /**
     * 校验rabbitMQ中获得的消息的格式
     *
     * @param drawPrizeParam
     * @return
     */
    @Override
    public boolean checkDrawPrizeMessage(DrawPrizeParam drawPrizeParam) {
        if (drawPrizeParam == null) {
            log.error(ServiceErrorCode.DRAW_PRIZE_PARAM_IS_EMPTY.getErrMsg());
            return false;
        }

        // 校验活动和奖品存在
        ActivityDO activityDO = activityMapper.getActivityDetail(drawPrizeParam.getActivityId());
        ActivityPrizeDO activityPrizeDO = activityPrizeMapper.getActivityPrize(drawPrizeParam.getActivityId(), drawPrizeParam.getPrizeId());
        if (activityPrizeDO == null || activityDO == null) {
            log.error(ServiceErrorCode.DRAW_PRIZE_ACTIVITY_OR_PRIZE_IS_EMPTY.getErrMsg());
            return false;
        }

        // 校验活动状态
        if (!activityDO.getStatus().equalsIgnoreCase(ActivityEnum.PROCEED.name())) {
            log.error(ServiceErrorCode.ACTIVITY_STATUS_ERROR.getErrMsg());
            return false;
        }

        // 校验奖品状态
        if (!activityPrizeDO.getStatus().equalsIgnoreCase(ActivityPrizeEnum.INIT.name())) {
            log.error(ServiceErrorCode.PRIZE_STATUS_ERROR.getErrMsg());
            return false;
        }

        // TODO 校验人员状态

        // 中奖者人数是否和设置奖品数量一致
        if (drawPrizeParam.getWinnerList().size() != activityPrizeDO.getPrizeAmount()) {
            log.error(ServiceErrorCode.WINNER_PRIZE_AMOUNT_ERROR.getErrMsg());
            return false;
        }
        return true;
    }

    /**
     * 存储中奖人员数据
     *
     * @param message
     * @return
     */
    @Override
    public List<WinnerDO> saveWinnerInfo(DrawPrizeParam message) {
        ActivityDO activityDO = activityMapper.getActivityDetail(message.getActivityId());
        ActivityPrizeDO activityPrizeDO = activityPrizeMapper.getActivityPrize(message.getActivityId(), message.getPrizeId());
        PrizeDO prizeDO = prizeMapper.selectById(message.getPrizeId());
        List<Long> userIds = message.getWinnerList().stream().map(user -> user.getUserId()).collect(Collectors.toList());
        List<UserDO> userDOS = userMapper.batchSelectByIds(userIds);

        // 一个活动的一个奖品的中奖人员信息
        List<WinnerDO> winnerDOS = userDOS.stream().map(userDO -> {
            WinnerDO winnerDO = new WinnerDO();

            winnerDO.setActivityId(activityDO.getId());
            winnerDO.setActivityName(activityDO.getActivityName());
            winnerDO.setPrizeId(prizeDO.getId());
            winnerDO.setPrizeName(prizeDO.getName());
            winnerDO.setPrizeTier(activityPrizeDO.getPrizeTiers());
            winnerDO.setWinnerId(userDO.getId());
            winnerDO.setWinnerName(userDO.getUserName());
            winnerDO.setWinnerEmail(userDO.getEmail());
            winnerDO.setWinnerPhoneNumber(userDO.getPhoneNumber());
            winnerDO.setWinningTime(message.getWinningTime());

            winnerDO.setId(userDO.getId());
            winnerDO.setGmtCreate(userDO.getGmtCreate());
            winnerDO.setGmtModified(userDO.getGmtModified());

            return winnerDO;
        }).collect(Collectors.toList());

        // 保存数据到数据库
        winnerRecordMapper.saveWinnerInfo(winnerDOS);

        // 存储数据到redis中
        // 基于奖品为单位的
        cacheWinningRecords(message.getActivityId() + "_" + prizeDO.getId(), winnerDOS, WINNING_RECORDS_TIMEOUT);

        // 基于活动为单位的
        List<WinnerDO> allWinnerDO = winnerRecordMapper.selectByActivityId(message.getActivityId());
        cacheWinningRecords(message.getActivityId() + "", allWinnerDO, WINNING_RECORDS_TIMEOUT);

        return winnerDOS;
    }


    /**
     * 将数据缓存到redis中
     *
     * @param key
     * @param winnerDOS
     * @param time
     */
    private void cacheWinningRecords(String key, List<WinnerDO> winnerDOS, Long time) {
        try {
            if (!StringUtils.hasText(key) || CollectionUtils.isEmpty(winnerDOS)) {
                log.error("key或winnerDOS为空，缓存失败");
                return;
            }

            redisUtil.set(WINNING_RECORDS_PREFIX + key, JacksonUtil.writeValueAsString(winnerDOS), time);
        } catch (Exception e) {
            log.error("缓存发生异常，缓存失败", e);
        }
    }


    /**
     * 从redis中获取中奖人员记录
     * @param key
     * @return
     */
    @Override
    public List<WinnerDO> getWinnerInfoList(String key) {
        try {
            if (!StringUtils.hasText(key)) {
                return Arrays.asList();
            }

            String ret = redisUtil.get(WINNING_RECORDS_PREFIX + key);
            if (!StringUtils.hasText(ret)) {
                return Arrays.asList();
            }

            return JacksonUtil.readListValue(ret, WinnerDO.class);
        } catch (Exception e) {
            log.info("从缓存中查询中奖记录异常");
            return Arrays.asList();
        }
    }


    /**
     * 回滚状态
     *
     * @param activityId
     * @param prizeId
     */
    @Override
    public void rollBackWinner(Long activityId, Long prizeId) {
        if (null == activityId) {
            log.error("要删除中奖记录相关的活动id为空！");
            return;
        }

        // 回滚数据库
        winnerRecordMapper.rollBack(activityId, prizeId);

        // 回滚redis
        if (prizeId != null) {
            redisUtil.delete(activityId + "_" + prizeId);
        }

        redisUtil.delete(String.valueOf(activityId));
    }


    /**
     * 获取活动获奖名单
     *
     * @param winnerRecordParam
     * @return
     */
    @Override
    public List<WinnerRecordInfoDTO> getWinnerRecord(WinnerRecordParam winnerRecordParam) {

        // 从redis中进行获取
        String key = winnerRecordParam.getPrizeId() == null
                ? String.valueOf(winnerRecordParam.getActivityId())
                : winnerRecordParam.getActivityId() + "_" + winnerRecordParam.getPrizeId();

        List<WinnerDO> winnerDOList = getWinnerInfoList(key);
        if(!winnerDOList.isEmpty()){
            return MyBeanUtil.transOfWinnerRecordInfoDTO(winnerDOList);
        }

        // redis中没有数据，从数据库中获取数据并保存到redis中
        List<WinnerDO> winnerDOS = winnerRecordMapper.getWinnerInfoByAPId(winnerRecordParam.getActivityId(), winnerRecordParam.getPrizeId());

        // 缓存数据到redis中
        cacheWinningRecords(key,winnerDOS,WINNING_RECORDS_TIMEOUT);

        return MyBeanUtil.transOfWinnerRecordInfoDTO(winnerDOS);
    }





}
