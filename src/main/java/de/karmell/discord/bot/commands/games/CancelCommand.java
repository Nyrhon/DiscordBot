package de.karmell.discord.bot.commands.games;

import de.karmell.discord.bot.commands.Command;
import de.karmell.discord.bot.games.Game;
import de.karmell.discord.bot.games.GameManager;
import de.karmell.discord.bot.util.MessageUtil;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CancelCommand extends Command {
    public CancelCommand() {
        super(new String[]{"cancel"}, CommandCategory.GAMES, "Cancels a game invitation.");
    }

    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(event.getChannelType().isGuild()) {
            Game g = GameManager.getInstance().getGame(event.getTextChannel());
            if(g != null && g.creator().getId().equals(event.getAuthor().getId()) && !g.hasStarted()) {
                GameManager.getInstance().removeGame(event.getTextChannel());
                event.getTextChannel().sendMessage(MessageUtil.simpleMessage("Game invitation has been cancelled.")).queue();
            }
        }
    }
}
