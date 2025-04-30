package com.test.lotterysystem.Service.Mq;

import cn.hutool.core.date.DateUtil;
import com.test.lotterysystem.Comment.Constants.Constants;
import com.test.lotterysystem.Comment.Exception.ServiceException;
import com.test.lotterysystem.Comment.Utils.JacksonUtil;
import com.test.lotterysystem.Comment.Utils.MailUtil;
import com.test.lotterysystem.Comment.Utils.MyCaptchaUtil;
import com.test.lotterysystem.Comment.Utils.SMSUtil;
import com.test.lotterysystem.Controller.Param.DrawPrizeParam;
import com.test.lotterysystem.Dao.DaoObject.ActivityPrizeDO;
import com.test.lotterysystem.Dao.DaoObject.WinnerDO;
import com.test.lotterysystem.Dao.Mapper.ActivityPrizeMapper;
import com.test.lotterysystem.Dao.Mapper.WinnerRecordMapper;
import com.test.lotterysystem.Service.ActivityService;
import com.test.lotterysystem.Service.ActivityStatusChange.ActivityStatusChange;
import com.test.lotterysystem.Service.DTO.ConvertActivityStatusDTO;
import com.test.lotterysystem.Service.DrawPrizeService;
import com.test.lotterysystem.Service.Enum.ActivityEnum;
import com.test.lotterysystem.Service.Enum.ActivityPrizeEnum;
import com.test.lotterysystem.Service.Enum.ActivityPrizeTiersEnum;
import com.test.lotterysystem.Service.Enum.ActivityUserEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 处理rabbitMQ中的消息
 */
@Component
@RabbitListener(queues = Constants.QUEUE_NAME)
@Slf4j
public class RabbitMqReceiver {

    @Autowired
    private DrawPrizeService drawPrizeService;

    @Autowired
    private ActivityStatusChange activityStatusChange;

    @Resource(name = "asyncServiceExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private SMSUtil smsUtil;

    @Autowired
    private ActivityPrizeMapper activityPrizeMapper;

    @Autowired
    private WinnerRecordMapper winnerRecordMapper;

    @Autowired
    private ActivityService activityService;


    /**
     * 接收消息并进行处理
     *
     * @param map
     */
    @RabbitHandler
    public void process(Map<String, String> map) throws Exception {
        log.info("接收方接收到数据...{}", map);
        DrawPrizeParam message = JacksonUtil.readValue(map.get("message"), DrawPrizeParam.class);

        try {
            // 校验接收到的消息
            if (!drawPrizeService.checkDrawPrizeMessage(message)) {
                return;
            }

            // 转换状态 —— 有依赖关系，先后顺序（责任链模式），并且多个状态的转换操作流程类似（策略模式）
            activityStatusChange.changeStatus(message);

            // 保存获奖人员数据
            List<WinnerDO> winnerDOS = drawPrizeService.saveWinnerInfo(message);

            // 发送消息提醒获奖者
            syncExecute(winnerDOS);

        } catch (ServiceException serviceException) {
            // 保证数据的一致性
            log.error("转换数据发生异常!", serviceException);
            rollback(message);
            throw serviceException;
        } catch (Exception exception) {
            // 保证数据的一致性
            log.error("处理 RabbitMQ 时发生异常!", exception);
            rollback(message);
            throw exception;
        }
    }


    /**
     * 回滚操作
     *
     * @param message
     */
    private void rollback(DrawPrizeParam message) {
        // 如果状态未改变，则不需要进行回滚操作
        if (!statusNeedRollback(message)) {
            return;
        }

        // 进行回滚操作
        statusRollback(message);

        // 判断获奖人员是否需要进行回滚操作
        if (!winnerNeedRollback(message)) {
            return;
        }

        // 进行回滚操作
        winnerRollback(message);
    }

    private void winnerRollback(DrawPrizeParam message) {
        drawPrizeService.rollBackWinner(message.getActivityId(),message.getPrizeId());
    }

    private boolean winnerNeedRollback(DrawPrizeParam message) {
        int num = winnerRecordMapper.countByActivityIdAndPrizeId(message.getActivityId(), message.getPrizeId());
        return num > 0;
    }

    private void statusRollback(DrawPrizeParam message) {
        ConvertActivityStatusDTO convertActivityStatusDTO = new ConvertActivityStatusDTO();
        convertActivityStatusDTO.setActivityId(message.getActivityId());
        convertActivityStatusDTO.setTargetActivityStatus(ActivityEnum.PROCEED);
        convertActivityStatusDTO.setPrizeId(message.getPrizeId());
        convertActivityStatusDTO.setTargetPrizeStatus(ActivityPrizeEnum.INIT);
        convertActivityStatusDTO.setUserIds(message.getWinnerList().stream().map(DrawPrizeParam.Winner::getUserId).collect(Collectors.toList()));
        convertActivityStatusDTO.setTargetUserStatus(ActivityUserEnum.INIT);

        // 恢复数据库数据
        activityStatusChange.rollbackStatus(convertActivityStatusDTO);

        // 恢复redis中的数据
        activityService.cacheActivity(message.getActivityId());
    }

    private boolean statusNeedRollback(DrawPrizeParam message) {
        ActivityPrizeDO activityPrize = activityPrizeMapper.getActivityPrize(message.getActivityId(), message.getPrizeId());
        if (activityPrize.getStatus().equalsIgnoreCase(ActivityPrizeEnum.COMPLETED.name())) {
            return true;
        }
        return false;
    }


    /**
     * 异步发送邮箱和手机通知
     *
     * @param winnerDOS
     */
    private void syncExecute(List<WinnerDO> winnerDOS) {
        // 发送短信通知
        threadPoolTaskExecutor.execute(() -> sendMessage(winnerDOS));

        // 发送邮箱通知
        threadPoolTaskExecutor.execute(() -> sendMail(winnerDOS));

    }


    /**
     * 发送短信通知
     *
     * @param winnerDOS
     */
    private void sendMail(List<WinnerDO> winnerDOS) {
        if (CollectionUtils.isEmpty(winnerDOS)) {
            log.error("winnerDOS为空，发送邮箱失败");
            return;
        }

        for (WinnerDO winnerDO : winnerDOS) {
            String context = "Hi，" + winnerDO.getWinnerName() + "。恭喜你在"
                    + winnerDO.getActivityName() + "活动中获得"
                    + ActivityPrizeTiersEnum.forName(winnerDO.getPrizeTier())
                    + "：" + winnerDO.getPrizeName() + "。获奖时间为"
                    + DateUtil.formatTime(winnerDO.getWinningTime()) + "，请尽快领 取您的奖励！";

            mailUtil.sendSampleMail(winnerDO.getWinnerEmail(), "中奖信息", context);
        }

    }


    /**
     * 发送邮箱通知
     *
     * @param winnerDOS
     */
    private void sendMessage(List<WinnerDO> winnerDOS) {
        if (CollectionUtils.isEmpty(winnerDOS)) {
            log.error("winnerDOS为空，发送邮箱失败");
            return;
        }

        for (WinnerDO winnerDO : winnerDOS) {
            Map<String, String> map = new HashMap<>();
            map.put("code", "1111");
            smsUtil.sendMessage("SMS_154950909",
                    winnerDO.getWinnerPhoneNumber().getValue(),
                    JacksonUtil.writeValueAsString(map));
        }
    }
}
