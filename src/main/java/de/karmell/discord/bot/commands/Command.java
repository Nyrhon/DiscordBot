package de.karmell.discord.bot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface Command {
    void invoke(String[] args, MessageReceivedEvent event);
    String describe();
}
