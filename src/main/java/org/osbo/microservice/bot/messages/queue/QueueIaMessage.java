package org.osbo.microservice.bot.messages.queue;

import org.osbo.microservice.bot.messages.pojos.MessageIa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class QueueIaMessage {
    @Autowired
    public JmsTemplate jmsTemplate;

    public void queueProcessMessage(@NonNull MessageIa message) {
        jmsTemplate.convertAndSend("queue.ia", message);
    }
}
