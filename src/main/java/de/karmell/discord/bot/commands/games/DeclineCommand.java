package de.karmell.discord.bot.commands.games;

import de.karmell.discord.bot.commands.Command;
import de.karmell.discord.bot.games.Game;
import de.karmell.discord.bot.games.GameManager;
import de.karmell.discord.bot.util.MessageUtil;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class DeclineCommand extends Command {
    public DeclineCommand() {
        super(new String[]{"decline"}, CommandCategory.GAMES, "Declines a pending game invitation.");
    }

    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(event.getChannelType().isGuild()) {
            Game g = GameManager.getInstance().getGame(event.getTextChannel());
            if(g != null && g.hasPlayer(event.getAuthor()) && !g.creator().getId().equals(event.getAuthor().getId())
                    && !g.hasStarted()) {
                GameManager.getInstance().removeGame(event.getTextChannel());
                event.getTextChannel().sendMessage(MessageUtil.simpleMessage(g.creator().getAsMention() +
                        " your game invitation has been declined.")).queue();
            }
        }
    }
}
