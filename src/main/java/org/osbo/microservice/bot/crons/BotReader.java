package org.osbo.microservice.bot.crons;

import org.osbo.core.bots.properties.PropertiesBot;
import org.springframework.stereotype.Component;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
@Component
public class BotReader {
    String token = PropertiesBot.getPropertie("bot.tokenprincipal");
    TelegramBot bot = new TelegramBot(token);

    public void process() {
        bot.setUpdatesListener(updates -> {
            updates.forEach(update -> {
                if (update.message() != null) {
                    System.out.println(update.message().chat().id()+" "+update.message().text());
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
