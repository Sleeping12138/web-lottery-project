package com.test.lotterysystem.Service.Mq;

import com.test.lotterysystem.Comment.Constants.Constants;
import com.test.lotterysystem.Comment.Utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = Constants.DLX_QUEUE_NAME)
@Slf4j
public class DlxRabbitMqReceiver {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitHandler
    public void process(Map<String, String> map){
        log.info("异常已修复...");
        rabbitTemplate.convertAndSend(Constants.EXCHANGE_NAME, Constants.KEY, map);
    }
}
