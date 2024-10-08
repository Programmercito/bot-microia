package org.osbo.microservice.bot.messages.listeners;

import org.osbo.core.arrays.ArrayLong;
import org.osbo.core.bots.properties.PropertiesBot;
import org.osbo.microservice.bot.messages.params.ModelosIa;
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
            if (!"".equals(chat.getContext())) {
                ollamaRequest.setContext(ArrayLong.getArrayLong(chat.getContext()));
            }
        } else {
            chat = new Chats();
            chat.setIduser(message.getIdchat());
            chat.setTipo(message.getTipo());
            chat.setCantidad(0);
        }
        chat.setUsando("SI");
        chatService.saveChat(chat);

        ollamaRequest.setModel(ModelosIa.getModelos().get(message.getTipo()));

        ollamaRequest.setPrompt(message.getMessage());
        HttpResponse<JsonNode> response = null;
        String deb = PropertiesBot.getPropertie("bot.debug");
        if (!"true".equals(deb)) {
            try {
                String ur = "http://" + message.getTipo() + ":11434/api/generate";
                System.out.println(ur);
                response = Unirest.post(ur)
                        .header("Content-Type", "application/json")
                        .body(ollamaRequest)
                        .asJson();
            } catch (UnirestException e) {
                response = null;
                e.printStackTrace();
            }
        }

        if (response != null || "true".equals(deb)) {
            try {
                String res = "";
                String context = "";
                if (!"true".equals(deb)) {
                    System.out.println(response.getStatus());

                    System.out.println(response.getBody().getObject().toString());
                    res = response.getBody().getObject().getString("response");
                    JSONArray arra = response.getBody().getObject().getJSONArray("context");
                    context = arra.join(",");
                } else {
                    res = "Soy la IA respondiendo";
                    context = "12";
                }
                chat.setCantidad(chat.getCantidad() + 1);
                chat.setContext(context);
                int mostrar = chat.getCantidad();
                if (chat.getCantidad() >= 5) {
                    chat.setCantidad(0);
                    chat.setContext("");
                }
                MessageSend respuestaia = new MessageSend();
                respuestaia.setIdchat(message.getIdchat());
                respuestaia.setMessage(res + " " + mostrar + "/5");
                queueSendMessage.queueProcessMessage(respuestaia);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error en la respuesta de la IA "+e.getMessage());
                MessageSend messerror = new MessageSend();
                messerror.setIdchat(message.getIdchat());
                messerror.setMessage("Error en la respuesta de la IA, por favor vuelva a intentar en unos momentos");
                queueSendMessage.queueProcessMessage(messerror);
            }

        } else {
            
            System.out.println("Error en la respuesta de la IA ");
            MessageSend messerror = new MessageSend();
            messerror.setIdchat(message.getIdchat());
            messerror.setMessage("Error en la respuesta de la IA, por favor vuelva a intentar en unos momentos");
            queueSendMessage.queueProcessMessage(messerror);
        }
        chat.setUsando("NO");
        chatService.saveChat(chat);

    }
}
