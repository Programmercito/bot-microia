package org.osbo.microservice.jms.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

@EnableJms
@Configuration
public class JmsConfiguration {
    @Bean
    public ActiveMQQueue queueProcess() {
        return new ActiveMQQueue("queue.process");
    }

    @Bean
    public ActiveMQQueue queueSend() {
        return new ActiveMQQueue("queue.send");
    }
}
