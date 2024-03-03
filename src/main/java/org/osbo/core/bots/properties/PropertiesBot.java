package org.osbo.core.bots.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesBot {
    static Properties prop = new Properties();

    static {
        try {
            InputStream inputStream = new FileInputStream("/opt/config/configv2.properties");
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPropertie(String key) {
        return prop.getProperty(key);
    }
}
