package com.johnymuffin.discordcore;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.util.logging.Level;


public class DiscordBot extends ListenerAdapter {
    public DiscordCore plugin;
    public JDA jda;


    public DiscordBot(DiscordCore main) {
        this.plugin = main;
    }

    public void startBot(String token) throws LoginException {
        jda = JDABuilder.createDefault(token).enableIntents(GatewayIntent.GUILD_MEMBERS).setMemberCachePolicy(MemberCachePolicy.ALL).build();
        jda.addEventListener(this);

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