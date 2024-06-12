package org.osbo.microservice.bot.messages.params;

import java.util.HashMap;
import java.util.Map;

public class ModelosIa {

    private static HashMap<String, String> modelosIa;

    static {
        modelosIa = new HashMap<String, String>();
        modelosIa.put("bot1llama", "llama2");
        modelosIa.put("bot2llama", "llama3");
    }

    public static Map<String, String> getModelos() {
        return modelosIa;
    }
}
