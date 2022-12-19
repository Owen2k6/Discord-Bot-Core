package com.johnymuffin.discordcore;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;

public class DCConfigReader {
    // New Config
    private String token = null;

    public DCConfigReader(DiscordCore plugin) throws Exception {
        File configFile = new File(plugin.getDataFolder(), "config.properties");

        Properties properties = new Properties();
        if (!configFile.exists()) {
            configFile.createNewFile();
        } else {
            properties.load(new FileInputStream(configFile));
        }

        if (properties.getProperty("token") == null) {
            properties.setProperty("token", "");
        } else {
            this.token = properties.getProperty("token");
        }

        properties.store(new FileOutputStream(configFile), plugin.getDescription().getFullName());
    }


    public String getToken() {
        return token;
    }


    public void onDisable() {
        token = null;
    }
}