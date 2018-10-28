package de.karmell.discord.bot.commands;

import de.karmell.discord.bot.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Map;

public class HelpCommand implements Command{
    public void invoke(String[] args, MessageReceivedEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.ORANGE);
        if(args.length > 0) {
            Command command = Main.COMMANDS.get(args[0]);
            if(command == null) {
                embedBuilder.addField("","Unknown command.", true);
            } else {
                embedBuilder.addField("Description for: " + args[0], command.describe(), true);
            }
        } else {
            embedBuilder.addField("Guild settings", "`prefix`: " + Main.CONFIG.COMMAND_PREFIX + "\n", false);
            StringBuilder commands = new StringBuilder();
            for(Map.Entry<String, Command> entry : Main.COMMANDS.entrySet()) {
                commands.append("`" + entry.getKey() + "`: " + entry.getValue().describe() + "\n");
            }
            embedBuilder.addField("Commands", commands.toString(), false);
        }
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    public String describe() {
        return "Help command";
    }
}
