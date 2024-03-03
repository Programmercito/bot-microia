package org.osbo.microservice.bot.messages.listeners;

import org.osbo.microservice.bot.messages.pojos.MessageProcess;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ReceiverProcess {
    @JmsListener(destination = "queue.process", containerFactory = "myFactory")
    public void receiveMessage(MessageProcess message) {
        System.out.println("Mensaje recibido");
        System.out.println(message.getMessage());
        System.out.println("mensaje procesado");
    }
}
