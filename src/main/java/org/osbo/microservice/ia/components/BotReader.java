package org.osbo.microservice.ia.components;

import org.osbo.microservice.ia.crons.BotService;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class BotReader {
    @PostConstruct
    public void init() {
        System.out.println("BotReader.init()");
        BotService botService = new BotService();
        botService.process();
    }
}
