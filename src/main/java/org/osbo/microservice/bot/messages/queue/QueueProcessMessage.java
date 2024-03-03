package org.osbo.microservice.bot.messages.queue;


import org.osbo.microservice.bot.messages.pojos.MessageProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class QueueProcessMessage {
    @Autowired
    public JmsTemplate jmsTemplate;
    
    public void queueProcessMessage(@NonNull MessageProcess message) {
        jmsTemplate.convertAndSend("queue.process",  message);
    }
}
