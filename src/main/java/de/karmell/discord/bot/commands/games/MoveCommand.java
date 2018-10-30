package de.karmell.discord.bot.commands.games;

import de.karmell.discord.bot.commands.Command;
import de.karmell.discord.bot.games.Game;
import de.karmell.discord.bot.games.GameManager;
import de.karmell.discord.bot.games.connectfour.ConnectFour;
import de.karmell.discord.bot.util.MessageUtil;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MoveCommand extends Command {
    public MoveCommand() {
        super(new String[]{"move", "m"}, CommandCategory.GAMES,
                "Performs a move in the current game (e.g. ConnectFour places a stone at given position x).");
    }

    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(event.getChannelType().isGuild()) {
            Game g = GameManager.getInstance().getGame(event.getTextChannel());
            if(g != null && g.hasPlayer(event.getAuthor()) && g.isTurn(event.getAuthor())) {
                switch(g.type()) {
                    case CONNECT_FOUR: moveC4(g, args, event);
                }
            }
        }
    }

    private void moveC4(Game g, String[] args, MessageReceivedEvent e) {
        if(args.length > 0) {
            int col = 0;
            try {
                col = Integer.parseInt(args[0]);
            } catch (NumberFormatException error) {
                e.getTextChannel().sendMessage(MessageUtil.errorMessage(args[0] + " is not a number!")).queue();
            }
            if(col < 1 || col > 7) {
                e.getTextChannel().sendMessage(MessageUtil.errorMessage(col + " is not a valid number (only 1-7 allowed)!")).queue();
            } else {
                ConnectFour game = (ConnectFour) g;
                boolean result = game.move(col);
                if(!result) {
                    e.getTextChannel().sendMessage(MessageUtil.errorMessage("There is no space in this column!")).queue();
                    game.switchTurns();
                }
                e.getTextChannel().sendMessage(game.getPlayFieldAsEmbed().build()).queue();
                if(game.terminated()) {
                    GameManager.getInstance().removeGame(e.getTextChannel());
                    e.getTextChannel().sendMessage(MessageUtil.simpleMessage("The game has ended!")).queue();
                }
            }
        } else {
            e.getTextChannel().sendMessage(MessageUtil.errorMessage("Please use the command in the form of: " +
                    "!move <column>. For further information use !help move")).queue();
        }
    }
}
