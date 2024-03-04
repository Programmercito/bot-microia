package org.osbo.microservice.bot.messages.listeners;

import org.osbo.microservice.bot.messages.pojos.MessageIa;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ReceiverIa {

    @JmsListener(destination = "queue.ia", containerFactory = "myFactory")
    public void consumeIa(MessageIa message) {

    }
}
