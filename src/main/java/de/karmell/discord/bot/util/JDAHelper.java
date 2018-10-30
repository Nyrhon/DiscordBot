package de.karmell.discord.bot.util;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class JDAHelper {
    public User getUserByName(JDA jda, String name, MessageReceivedEvent event) {
        User[] users = new User[1];
        jda.getUsers().forEach((user) -> {
            if(user.getName().toLowerCase().matches(".*" + name.toLowerCase() + ".*") && event.getGuild().isMember(user)) {
                users[0] = user;
            }
        });
        return users[0];
    }
}
