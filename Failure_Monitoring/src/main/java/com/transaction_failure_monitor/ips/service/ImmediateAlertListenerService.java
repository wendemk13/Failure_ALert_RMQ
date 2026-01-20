package com.transaction_failure_monitor.ips.service;

import com.rabbitmq.client.Channel;
import com.transaction_failure_monitor.ips.config.RMQConfig;
import com.transaction_failure_monitor.ips.entity.TransactionAlerts;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface ImmediateAlertListenerService {
    @RabbitListener(queues = RMQConfig.TRANSACTION_QUEUE, ackMode = "MANUAL")
    void handleImmediateAlertMessage(TransactionAlerts message, Channel channel,
                                     @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException;
}
