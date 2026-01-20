package com.transaction_failure_monitor.ips.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RMQConfig {
    public static final String TRANSACTION_ALERT_EXCHANGE = "transaction.alerting.exchange.ips";
    public static final String TRANSACTION_DLQ_EXCHANGE = "transaction.alert.exchange.ips.dlq";
    public static final String TRANSACTION_QUEUE = "transaction.alert.ips.queue";
    public static final String TRANSACTION_DLQ_QUEUE = "transaction.alert.ips.dlq";

    @Bean
    public FanoutExchange transactionAlertExchange() {
        return new FanoutExchange(TRANSACTION_ALERT_EXCHANGE);
    }

    @Bean
    public DirectExchange transactionDlqExchange() {
        return new DirectExchange(TRANSACTION_DLQ_EXCHANGE);
    }

    @Bean
    public Queue transactionQueue() {
        return QueueBuilder.durable(TRANSACTION_QUEUE)
                .withArgument("x-dead-letter-exchange", TRANSACTION_DLQ_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", TRANSACTION_DLQ_QUEUE)
                .build();
    }

    @Bean
    public Queue transactionDlqQueue() {
        return new Queue(TRANSACTION_DLQ_QUEUE);
    }

    @Bean
    public Binding transactionAlertBinding() {
        return BindingBuilder.bind(transactionQueue()).to(transactionAlertExchange());
    }

    @Bean
    public Binding transactionDlqBinding() {
        return BindingBuilder.bind(transactionDlqQueue()).to(transactionDlqExchange()).with(TRANSACTION_DLQ_QUEUE);
    }

    @Bean
    public MessageConverter MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
