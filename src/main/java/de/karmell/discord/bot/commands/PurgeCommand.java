package de.karmell.discord.bot.commands;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class PurgeCommand implements Command {
    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(event.getTextChannel().getType().isGuild()) {
            if(args.length > 0) {
                int num = 50;
                try {
                    num = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    event.getChannel().sendMessage("Invalid number specified.").queue();
                }
                if(num < 1 || num > 100) {
                    event.getChannel().sendMessage("Number exceeding range of 1-100").queue();
                } else {
                    try {
                        MessageHistory history = new MessageHistory(event.getTextChannel());
                        List<Message> messages = history.retrievePast(num).complete();
                        event.getTextChannel().deleteMessages(messages).queue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    @Override
    public String describe() {
        return "Purges the last x messages";
    }
}
