package de.karmell.discord.bot.commands.general;

import de.karmell.discord.bot.Bot;
import de.karmell.discord.bot.core.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class StopCommand extends Command {
    public StopCommand() {
        super(new String[]{"stop"}, CommandCategory.GENERAL, "Stops the bot.");
    }

    public void invoke(String[] args, MessageReceivedEvent event) {
        if(!event.getAuthor().getId().equals(Bot.getConfig().BOT_OWNER)) {
            event.getChannel().sendMessage("You're not my dad! :baby:").queue();
        } else {
            event.getJDA().shutdownNow();
            System.exit(0);
        }
    }
}