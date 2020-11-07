package de.karmell.discord.bot.util;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class JDAHelper {
    public static User getUserByName(JDA jda, String name, MessageReceivedEvent event) {
        User[] users = new User[1];
        jda.getUsers().forEach((user) -> {
            if(user.getName().toLowerCase().matches(".*" + name.toLowerCase() + ".*")
                && event.getGuild().isMember(user)) {
                users[0] = user;
            }
        });
        return users[0];
    }

    public static TextChannel getTextChannelByNameOrId(String search, MessageReceivedEvent event) {
        String id = null;
        if(search.startsWith("id:")) {
            id = search.substring(search.indexOf(":") + 1);
        }
        if(id != null) {
            return event.getGuild().getTextChannelById(id);
        }
        List<TextChannel> txs = event.getGuild().getTextChannels();
        for(TextChannel tx : txs) {
            if(tx.getName().toLowerCase().equals(search.toLowerCase())) {
                return tx;
            }
        }
        return null;
    }
}
