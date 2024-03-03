package org.osbo.microservice.bot.reader;

import org.osbo.microservice.bot.crons.BotReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class BotExecutor {
    @Autowired
    private BotReader botService;

    @PostConstruct
    public void init() {
        botService.process();
    }
}
