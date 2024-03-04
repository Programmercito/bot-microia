package org.osbo.microservice.bot.messages.listeners;

import org.osbo.microservice.bot.messages.pojos.MessageProcess;
import org.osbo.microservice.bot.messages.pojos.MessageSend;
import org.osbo.microservice.bot.messages.queue.QueueSendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ReceiverProcess {
    @Autowired
    private QueueSendMessage queueSendMessage;

    @JmsListener(destination = "queue.process", containerFactory = "myFactory")
    public void receiveMessage(MessageProcess message) {
        System.out.println("Mensaje recibido");
        MessageSend messageSend = new MessageSend();
        messageSend.setIdchat(message.getIdchat());
        String me = "";
        if (message.getMessage().startsWith("/start") || message.getMessage().startsWith("/help")) {
            me = "Hola, como estas? Yo soy un bot con IA para responderte a diferentes cosas cuento con los siguientes servicios: \n"
                    +
                    "1. /init: Iniciar el bot\n" +
                    "2. /stop: Detener el bot\n" +
                    "3. /help: Ayuda\n";
            messageSend.setMessage(me);
            queueSendMessage.queueProcessMessage(messageSend);
        }else if (message.getMessage().startsWith("/init")) {
            me = "Bots con IA disponibles:\n" +
            "1. /bot1 LLama2 general\n" +
            "Pronto mas !!\n";
            messageSend.setMessage(me);
            queueSendMessage.queueProcessMessage(messageSend);
        } else if (message.getMessage().startsWith("/stop")) {
            me = "Bot detenido";
            messageSend.setMessage(me);
            queueSendMessage.queueProcessMessage(messageSend);
        } else if (message.getMessage().startsWith("/")) {
            me = "Comando no reconocido";
            messageSend.setMessage(me);
            queueSendMessage.queueProcessMessage(messageSend);
        }  else {
            messageSend.setMessage("No entiendo el mensaje");
            messageSend.setMessage(me);
            queueSendMessage.queueProcessMessage(messageSend);
        }
        System.out.println("mensaje procesado");
    }
}
