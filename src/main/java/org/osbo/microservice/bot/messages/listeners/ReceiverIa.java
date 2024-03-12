package org.osbo.microservice.bot.messages.listeners;

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
        Unirest.config().connectTimeout(1000000);
        Chats chat = chatService.getChatByIdUserAndTipo(message.getIdchat(), message.getTipo());
        OllamaRequest ollamaRequest = new OllamaRequest();

        if (chat != null) {
            ollamaRequest.setContext(chat.getContext());
        }
        if (message.getTipo().equals("bot1llama")) {
            ollamaRequest.setModel("llama2");
            ollamaRequest.setSystem("");
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (response != null) {
            String res = response.getBody().getObject().getString("response");
            String context = response.getBody().getObject().getJSONArray("context").join(",");
            if (chat == null) {
                chat = new Chats();
                chat.setIduser(message.getIdchat());
                chat.setTipo(message.getTipo());
            }
            chat.setContext(context);
            chatService.saveChat(chat);
            MessageSend respuestaia = new MessageSend();
            respuestaia.setIdchat(message.getIdchat());
            respuestaia.setMessage(res);
            queueSendMessage.queueProcessMessage(respuestaia);

        } else {
            System.out.println("Error en la respuesta de la IA");
            MessageSend messerror = new MessageSend();
            message.setIdchat(message.getIdchat());
            message.setMessage("Error en la respuesta de la IA, por favor vuelva a intentar en unos momentos");
            queueSendMessage.queueProcessMessage(messerror);
        }


    }
}
