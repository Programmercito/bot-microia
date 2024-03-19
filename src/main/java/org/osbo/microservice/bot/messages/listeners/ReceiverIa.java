package org.osbo.microservice.bot.messages.listeners;

import java.util.stream.IntStream;

import org.osbo.core.arrays.ArrayLong;
import org.osbo.microservice.bot.messages.pojos.MessageIa;
import org.osbo.microservice.bot.messages.pojos.MessageSend;
import org.osbo.microservice.bot.messages.pojos.OllamaRequest;
import org.osbo.microservice.bot.messages.queue.QueueSendMessage;
import org.osbo.microservice.model.entities.Chats;
import org.osbo.microservice.model.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONArray;

@Component
public class ReceiverIa {
    @Autowired
    ChatService chatService;

    @Autowired
    QueueSendMessage queueSendMessage;

    @JmsListener(destination = "queue.ia", containerFactory = "myFactory")
    public void consumeIa(MessageIa message) {
        System.out.println("Mensaje recibido por la IA");
        System.out.println("Mensaje: " + message.getMessage());
        System.out.println("Tipo: " + message.getTipo());
        Chats chat = chatService.getChatByIdUserAndTipo(message.getIdchat(), message.getTipo());
        OllamaRequest ollamaRequest = new OllamaRequest();

        if (chat != null) {
            ollamaRequest.setContext(ArrayLong.getArrayLong(chat.getContext()));
        }
        if (message.getTipo().equals("bot1llama")) {
            ollamaRequest.setModel("llama2");
            ollamaRequest.setSystem("talk in spanish how to mario bros");
        }
        ollamaRequest.setPrompt(message.getMessage());
        HttpResponse<JsonNode> response = null;
        try {
            String ur = "http://" + message.getTipo() + ":11434/api/generate";
            response = Unirest.post(ur)
                    .header("Content-Type", "application/json")
                    .body(ollamaRequest)
                    .asJson();
        } catch (UnirestException e) {
            response = null;
            e.printStackTrace();
        }

        if (response != null) {
            String res = response.getBody().getObject().getString("response");
            JSONArray arra = response.getBody().getObject().getJSONArray("context");
            String context = arra.join(",");
            if (chat == null) {
                chat = new Chats();
                chat.setIduser(message.getIdchat());
                chat.setTipo(message.getTipo());
                chat.setCantidad( 0);
            }
            chat.setCantidad(chat.getCantidad() + 1);
            chat.setContext(context);
            if (chat.getCantidad() >= 15) {
                chat.setCantidad(0);
                chat.setContext("");
            }
            chatService.saveChat(chat);
            MessageSend respuestaia = new MessageSend();
            respuestaia.setIdchat(message.getIdchat());
            respuestaia.setMessage(res + " " + chat.getCantidad() + "/15");
            queueSendMessage.queueProcessMessage(respuestaia);

        } else {
            System.out.println("Error en la respuesta de la IA");
            MessageSend messerror = new MessageSend();
            messerror.setIdchat(message.getIdchat());
            messerror.setMessage("Error en la respuesta de la IA, por favor vuelva a intentar en unos momentos");
            queueSendMessage.queueProcessMessage(messerror);
        }

    }
}
