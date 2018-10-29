package de.karmell.discord.bot.util;

import java.io.*;
import java.util.Properties;

/**
 * Wrapper class to load a properties file and parse the content as static attributes.
 */
public class Config {
    public final String BOT_TOKEN;
    public final String COMMAND_PREFIX;
    public final String GAME;
    public final String BOT_OWNER;

    public Config() {
        File configFile = new File("config.properties");
        Properties properties = new Properties();
        if(!configFile.exists()) {
            try {
                configFile.createNewFile();
                properties.load(new BufferedInputStream(new FileInputStream(configFile)));
                properties.setProperty("bot_token", "");
                properties.setProperty("command_prefix", "!");
                properties.setProperty("game", "!help");
                properties.setProperty("bot_owner", "");
                FileOutputStream outputStream = new FileOutputStream(configFile);
                properties.store(outputStream, null);
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                properties.load(new BufferedInputStream(new FileInputStream(configFile)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BOT_TOKEN = properties.getProperty("bot_token");
        COMMAND_PREFIX = properties.getProperty("command_prefix");
        GAME = properties.getProperty("game");
        BOT_OWNER = properties.getProperty("bot_owner");
    }
}
