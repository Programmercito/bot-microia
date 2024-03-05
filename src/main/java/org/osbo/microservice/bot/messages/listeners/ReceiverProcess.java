package org.osbo.microservice.bot.messages.listeners;

import org.osbo.microservice.bot.messages.pojos.MessageProcess;
import org.osbo.microservice.bot.messages.pojos.MessageSend;
import org.osbo.microservice.bot.messages.queue.QueueSendMessage;
import org.osbo.microservice.model.entities.Users;
import org.osbo.microservice.model.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ReceiverProcess {
    @Autowired
    private QueueSendMessage queueSendMessage;
    @Autowired
    private UsersService usersService;

    @JmsListener(destination = "queue.process", containerFactory = "myFactory")
    public void receiveMessage(MessageProcess message) {
        System.out.println("Mensaje recibido");
        MessageSend messageSend = new MessageSend();
        messageSend.setIdchat(message.getIdchat());
        String me = "";
        Users user = new Users();
        if (message.getMessage().startsWith("/")) {
            user = usersService.getUser(message.getIdchat());
            if (user == null) {
                user = new Users();
                user.setId(message.getIdchat());
                user.setUsername(message.getUsername());
                user.setEstado("activo");
                usersService.saveUser(user);
                System.out.println("Usuario nuevo guardado");
            } else {
                user.setEstado("activo");
                usersService.saveUser(user);
            }
        }
        if (message.getMessage().startsWith("/start") || message.getMessage().startsWith("/help")) {
            String us = user.getUsername();
            me = "Hola " + us
                    + ", como estas? Yo soy un bot con IA para responderte a diferentes cosas cuento con los siguientes servicios: \n"
                    +
                    "1. /init: Iniciar el bot\n" +
                    "2. /stop: Detener el bot\n" +
                    "3. /help: Ayuda\n";
            messageSend.setMessage(me);
            queueSendMessage.queueProcessMessage(messageSend);
        } else if (message.getMessage().startsWith("/init")) {
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
        } else {
            messageSend.setMessage("No entiendo el mensaje");
            messageSend.setMessage(me);
            queueSendMessage.queueProcessMessage(messageSend);
        }
        System.out.println("mensaje procesado");
    }
}
