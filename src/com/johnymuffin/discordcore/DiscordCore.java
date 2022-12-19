package com.johnymuffin.discordcore;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiscordCore extends JavaPlugin implements Listener {
    //Basic Plugin Info
    private static DiscordCore plugin;
    private Logger log;
    private String pluginName;
    private PluginDescriptionFile pdf;
    //Plugin Fields
    private DiscordBot discord;


    public void onEnable() {
        plugin = this;
        log = Bukkit.getServer().getLogger();
        pdf = this.getDescription();
        pluginName = pdf.getName();
        logInfo(Level.INFO, "Is Loading, Version: " + pdf.getVersion() + ".");
        logInfo(Level.INFO, "THIS PLUGIN IS LICENSED UNDER GNU.");

        //Config Information Start
        String softToken = null;
        try {
            File file = new File(plugin.getDataFolder(), "config.yml");
            file.getParentFile().mkdirs();
            Configuration configuration = new Configuration(file);
            configuration.load();
            if (configuration.getProperty("token") == null || configuration.getString("token").equalsIgnoreCase("token") || configuration.getString("token").isEmpty()) {
                configuration.setProperty("token", "token");
                configuration.save();
                Bukkit.getServer().getPluginManager().disablePlugin(plugin);
            } else {
                softToken = (String) configuration.getProperty("token");
            }
        } catch (Exception e) {
            try {
                DCConfigReader dcConfigReader = new DCConfigReader(plugin);
                softToken = dcConfigReader.getToken();
            } catch (Exception e2) {
                logInfo(Level.WARNING, "Unable to use backup ");
            }
        }

        if(softToken == null || softToken.equalsIgnoreCase("token") || softToken.isEmpty()) {
            plugin.logInfo(Level.WARNING, "Failed to find a Discord token in the config file, shutting down.");
            Bukkit.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        //Config Information End
        logInfo(Level.INFO, "Starting internal Discord Bot.");
        try {
            discord = new DiscordBot(this);
            discord.startBot(softToken);
        } catch (Exception e) {
            logInfo(Level.WARNING, e + ": " + e.getMessage());
            Bukkit.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }
    }

    public void onDisable() {
        logInfo(Level.INFO, "Disabling plugin.");
        if (discord != null) {
            DiscordShutdownEvent shutdownEvent = new DiscordShutdownEvent();
            Bukkit.getServer().getPluginManager().callEvent(shutdownEvent);

            discord.discordBotStop();
        }
        logInfo(Level.INFO, "Disabled.");
    }

    @Deprecated
    public DiscordBot Discord() {
        //Return Bot for other plugins
        return discord;
    }

    public DiscordBot getDiscordBot() {
        return discord;
    }

    public void logInfo(Level level, String s) {
        log.log(level, "[" + pluginName + "] " + s);
    }


}
