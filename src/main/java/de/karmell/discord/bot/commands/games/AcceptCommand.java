package de.karmell.discord.bot.commands.games;

import de.karmell.discord.bot.commands.Command;
import de.karmell.discord.bot.games.Game;
import de.karmell.discord.bot.games.GameManager;
import de.karmell.discord.bot.games.connectfour.ConnectFour;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AcceptCommand extends Command {
    public AcceptCommand() {
        super(new String[]{"accept"}, CommandCategory.GAMES, "Accepts a pending invitation.");
    }

    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(event.getChannelType().isGuild()) {
            Game g = GameManager.getInstance().getGame(event.getTextChannel());
            if(g != null && !g.hasStarted() && g.hasPlayer(event.getAuthor())
                    && !g.creator().getId().equals(event.getAuthor().getId())) {
                switch(g.type()) {
                    case CONNECT_FOUR: acceptC4(g, event);
                }
            }
        }
    }

    private void acceptC4(Game g, MessageReceivedEvent e) {
        ConnectFour game = (ConnectFour) g;
        e.getTextChannel().sendMessage("The game between " + game.creator().getAsMention() + " and " + e.getAuthor().getAsMention() + " has begun!").queue();
        e.getTextChannel().sendMessage(game.getPlayFieldAsEmbed().build()).queue();
        game.start();
    }
}
