package com.johnymuffin.discordcore;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.bukkit.util.config.Configuration;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.logging.Level;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;
import static net.dv8tion.jda.api.interactions.commands.OptionType.USER;


public class DiscordBot extends ListenerAdapter {
    public DiscordCore plugin;
    public JDA jda;


    public DiscordBot(DiscordCore main) {
        this.plugin = main;
    }

    public void startBot(String token) throws LoginException {
        jda = JDABuilder.createDefault(token).enableIntents(GatewayIntent.GUILD_MEMBERS).setMemberCachePolicy(MemberCachePolicy.ALL).build();
        jda.addEventListener(this);

        //Initialise Slash Commands
        CommandListUpdateAction commands = jda.updateCommands();

        commands.addCommands(
                new CommandData("online", "List all online players.")
        );
        commands.addCommands(
                new CommandData("link", "Link your Minecraft account to discord")
                        .addOptions(new OptionData(STRING, "username", "Please enter a username in order to link.")
                                .setRequired(true))
        );
        commands.addCommands(
                new CommandData("status", "Link your Minecraft account to discord")
                        .addOptions(new OptionData(USER, "user", "Choose a user you wish to see the status of.")
                                .setRequired(true))
        );
        commands.addCommands(
                new CommandData("stats", "View the stats of a player on the server")
                        .addOptions(new OptionData(USER, "username", "Who do you want to see the stats of? (CaSe SeNsItIvE)")
                                .setRequired(true))
        );
        commands.addCommands(
                new CommandData("unlink", "Unlink your discord account from any linked minecraft accounts.")
        );

        commands.queue();


    }

    public void discordBotStop() {
        System.out.println("Discord Bot Will Begin Shutdown: " + jda.getStatus());
        jda.shutdownNow();

    }

    @Deprecated
    public void DiscordSendToChannel(String channel, String message) {
        this.discordSendToChannel(channel, message);
    }


    public void discordSendToChannel(String channel, String message) {
        if (jda.getStatus() == JDA.Status.CONNECTED) {
            TextChannel textChannel = jda.getTextChannelById(channel);
            textChannel.sendMessage(message).queue();
        } else {
            System.out.println("Message is unable to send, Discord still starting: " + jda.getStatus());
        }

    }

    public JDA getJda() {
        return jda;
    }

    public void onReady(ReadyEvent event) {
        plugin.logInfo(Level.INFO, "Discord Bot (" + event.getJDA().getSelfUser().getName() + "#" + event.getJDA().getSelfUser().getDiscriminator() + ") connected to " + event.getGuildTotalCount() + " guilds.");
    }
}