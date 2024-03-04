package org.osbo.microservice.bot.messages.listeners;

import org.osbo.core.bots.properties.PropertiesBot;
import org.osbo.core.bots.slepper.Sleep;
import org.osbo.microservice.bot.messages.pojos.MessageSend;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

@Component
public class ReceiverForSend {
    @JmsListener(destination = "queue.send", containerFactory = "myFactory")
    public void sendMessage(MessageSend message) {
        Sleep.sleep1seg();
        System.out.println("llegando a cola de envio");
        String token = PropertiesBot.getPropertie("bot.tokenprincipal");
        TelegramBot bot = new TelegramBot(token);
        SendResponse response = bot.execute(new SendMessage(message.getIdchat(), message.getMessage()));
        if (response.isOk()) {
            System.out.println("Mensaje enviado");
        } else {
            System.out.println("Error al enviar mensaje");
        }
    }
}
