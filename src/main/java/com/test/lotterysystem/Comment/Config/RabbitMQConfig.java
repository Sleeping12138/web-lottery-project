package com.test.lotterysystem.Comment.Config;

import com.rabbitmq.client.AMQP;
import com.test.lotterysystem.Comment.Constants.Constants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    /**
     * 普通队列配置
     * @return
     */
    // 创建队列
    @Bean(Constants.QUEUE_NAME)
    public Queue directQueue(){
        return QueueBuilder.durable(Constants.QUEUE_NAME)
                .deadLetterExchange(Constants.DLX_EXCHANGE_NAME )
                .deadLetterRoutingKey(Constants.DLX_KEY).build();
    }


    // 创建交换机
    @Bean(Constants.EXCHANGE_NAME)
    public DirectExchange directExchange(){
        return ExchangeBuilder.directExchange(Constants.EXCHANGE_NAME).durable(true).build();
    }


    // 创建绑定关系
    @Bean(Constants.KEY)
    public Binding directQueueBinding(@Qualifier(Constants.QUEUE_NAME) Queue queue,@Qualifier(Constants.EXCHANGE_NAME) DirectExchange directExchange){
        return BindingBuilder.bind(queue).to(directExchange).with(Constants.KEY);
    }


    /**
     * 死信队列配置
     *
     */
    // 创建死信队列
    @Bean(Constants.DLX_QUEUE_NAME)
    public Queue dlxQueue(){
        return QueueBuilder.durable(Constants.DLX_QUEUE_NAME).build();
    }

    // 创建死信交换机
    @Bean(Constants.DLX_EXCHANGE_NAME)
    public DirectExchange dlxExchange(){
        return ExchangeBuilder.directExchange(Constants.DLX_EXCHANGE_NAME).build();
    }

    // 绑定死信队列
    @Bean(Constants.DLX_KEY)
    public Binding dlxQueueBinding(@Qualifier(Constants.DLX_QUEUE_NAME) Queue queue, @Qualifier(Constants.DLX_EXCHANGE_NAME) DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(Constants.DLX_KEY);
    }

    /**
     * 数据格式转换
     *
     */

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,Jackson2JsonMessageConverter jsonMessageConverter){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        return rabbitTemplate;
    }

    // 为了让存储到rabbitMQ中的数据可被读
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
