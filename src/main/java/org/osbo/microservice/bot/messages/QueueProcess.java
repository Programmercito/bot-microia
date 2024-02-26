package org.osbo.microservice.bot.messages;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class QueueProcess {
    @Autowired
    public JmsTemplate jmsTemplate;
    
    public void queueProcess(@NonNull MessageProcess message) {
        jmsTemplate.convertAndSend("queue.process",  message);
    }
}
