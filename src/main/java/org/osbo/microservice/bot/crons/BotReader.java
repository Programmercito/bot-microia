package org.osbo.microservice.bot.crons;

import org.osbo.core.bots.properties.PropertiesBot;
import org.osbo.microservice.bot.messages.pojos.MessageProcess;
import org.osbo.microservice.bot.messages.queue.QueueProcessMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;

import jakarta.annotation.PostConstruct;

@Component
public class BotReader {
    String token = PropertiesBot.getPropertie("bot.tokenprincipal");
    TelegramBot bot = new TelegramBot(token);

    @Autowired
    QueueProcessMessage queueProcess;

    @PostConstruct
    public void process() {
        bot.setUpdatesListener(updates -> {
            updates.forEach(update -> {
                if (update.message() != null) {
                    MessageProcess message = new MessageProcess();
                    message.setIdchat(update.message().chat().id());
                    message.setMessage(update.message().text());
                    message.setUsername(update.message().chat().username());
                    queueProcess.queueProcessMessage(message);
                }
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, e -> {
            if (e.response() != null) {
                e.response().errorCode();
                e.response().description();
            } else {
                e.printStackTrace();
            }
        });
    }
}
