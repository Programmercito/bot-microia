package org.osbo.microservice.bot.messages.listeners;

import java.util.Date;

import org.osbo.core.date.DateAdd;
import org.osbo.core.date.DateFormatter;
import org.osbo.microservice.bot.messages.pojos.MessageIa;
import org.osbo.microservice.bot.messages.pojos.MessageProcess;
import org.osbo.microservice.bot.messages.pojos.MessageSend;
import org.osbo.microservice.bot.messages.queue.QueueIaMessage;
import org.osbo.microservice.bot.messages.queue.QueueSendMessage;
import org.osbo.microservice.model.entities.Chats;
import org.osbo.microservice.model.entities.Servicios;
import org.osbo.microservice.model.entities.Users;
import org.osbo.microservice.model.services.ChatService;
import org.osbo.microservice.model.services.ServiciosService;
import org.osbo.microservice.model.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ReceiverProcess {

    @Autowired
    private ChatService chatService;
    @Autowired
    private QueueSendMessage queueSendMessage;
    @Autowired
    private QueueIaMessage queueIaMessage;
    @Autowired
    private UsersService usersService;
    @Autowired
    private ServiciosService serviciosService;

    @JmsListener(destination = "queue.process", containerFactory = "myFactory")
    public void receiveMessage(MessageProcess message) {
        System.out.println("Mensaje recibido");

        MessageSend messageSend = new MessageSend();
        messageSend.setIdchat(message.getIdchat());
        String me = "";
        Users user = new Users();
        user = usersService.getUser(message.getIdchat());
        if (message.getMessage().startsWith("/")) {
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
                    "2. /stop: Reiniciar conversacion con el bot\n" +
                    "3. /help: Ayuda\n";
            messageSend.setMessage(me);
            queueSendMessage.queueProcessMessage(messageSend);
        } else if (message.getMessage().startsWith("/init")) {
            me = "Bots con IA disponibles:\n" +
                    "1. /bot1llama LLama2 general\n" +
                    "2. /bot2llama LLama3 general\n" +
                    "Pronto mas !!\n";
            messageSend.setMessage(me);
            queueSendMessage.queueProcessMessage(messageSend);
        } else if (message.getMessage().startsWith("/stop")) {
            Chats chat = chatService.getChatByIdUserAndTipo(message.getIdchat(), user.getComando());
            if (chat != null) {
                chat.setUsando("NO");
                chat.setContext("");
                chat.setCantidad(1);
                chatService.saveChat(chat);
            }
            me = "Conversacion reseteada";
            messageSend.setMessage(me);
            queueSendMessage.queueProcessMessage(messageSend);
        } else if (message.getMessage().startsWith("/bot1llama") || message.getMessage().startsWith("/bot2llama")) {
            me = "Bot activado";
            user.setComando(message.getMessage().substring(1, 10));
            me = me + " " + user.getComando() + " ";
            usersService.saveUser(user);
            Servicios servicio;
            servicio = serviciosService.getServicioByUserAndService(message.getIdchat(), "botFree");
            if (servicio == null) {
                servicio = new Servicios();
                servicio.setIduser(message.getIdchat());
                servicio.setServicio(message.getMessage().substring(1, 10));
                servicio.setEstado("activo");
                servicio.setFecha_inicio(new Date());
                servicio.setFecha_fin(DateAdd.agregarMeses(new Date(), 3));
                servicio.setRegistro(new Date());
                serviciosService.saveServicio(servicio);
            } else if (servicio.getFecha_fin().getTime() < new Date().getTime()) {
                servicio.setEstado("activo");
                servicio.setFecha_inicio(new Date());
                servicio.setFecha_fin(DateAdd.agregarMeses(servicio.getFecha_fin(), 3));
                serviciosService.saveServicio(servicio);
            }
            me = me + " hasta el " + DateFormatter.formatDate(servicio.getFecha_fin());
            me = me + "\n Luego puede renovar sin problema, dime en que te puedo ayudar?";
            messageSend.setMessage(me);
            queueSendMessage.queueProcessMessage(messageSend);
        } else if (message.getMessage().startsWith("/")) {
            me = "Comando no reconocido";
            messageSend.setMessage(me);
            queueSendMessage.queueProcessMessage(messageSend);
        } else {
            if (user != null && user.getComando() != null && user.getComando().equals("bot1llama")
                    || user.getComando().equals("bot2llama")) {
                Chats chat = chatService.getChatByIdUserAndTipo(message.getIdchat(), user.getComando().substring(0, 9));
                boolean porcesando = false;
                if (chat != null && "SI".equals(chat.getUsando())) {
                    porcesando = true;
                }
                if (porcesando) {
                    messageSend.setMessage(
                            "Se esta procesando su mensaje favor esperar a que responsadamos o intente de nuevo mas tarde");
                    queueSendMessage.queueProcessMessage(messageSend);
                } else {
                    MessageIa messageIa = new MessageIa();
                    messageIa.setMessage(message.getMessage());
                    messageIa.setIdchat(message.getIdchat());
                    messageIa.setTipo(user.getComando().substring(0, 9));
                    queueIaMessage.queueProcessMessage(messageIa);
                }
            } else {
                messageSend.setMessage("No entiendo el mensaje, si tienes dudas usa /start para mas opciones");
                queueSendMessage.queueProcessMessage(messageSend);
            }
        }
        System.out.println("mensaje procesado");
    }
}
