package org.osbo.microservice.bot.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class QueueSend {
    @Autowired
    public JmsTemplate jmsTemplate;

    public void queueProcess(@NonNull MessageSend message) {
        jmsTemplate.convertAndSend("queue.send", message);
    }
}
