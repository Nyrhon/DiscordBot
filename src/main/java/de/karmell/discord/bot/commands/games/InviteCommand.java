package de.karmell.discord.bot.commands.games;

import de.karmell.discord.bot.commands.Command;
import de.karmell.discord.bot.games.Game;
import de.karmell.discord.bot.games.GameManager;
import de.karmell.discord.bot.games.GameType;
import de.karmell.discord.bot.games.connectfour.ConnectFour;
import de.karmell.discord.bot.util.JDAHelper;
import de.karmell.discord.bot.util.MessageUtil;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;

public class InviteCommand extends Command {
    public InviteCommand() {
        super(new String[]{"invite"}, CommandCategory.GAMES, "Invites a person to a game. Usage: !invite *game* *person* (Games: 0 - ConnectFour).");
    }

    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(event.getChannelType().isGuild()) {
            Game g = GameManager.getInstance().getGame(event.getTextChannel());
            if(g == null) {
                if(args.length > 1) {
                    int type = -1;
                    try {
                        type = Integer.parseInt(args[0]);
                    } catch (NumberFormatException error) {
                        event.getTextChannel().sendMessage(
                                MessageUtil.errorMessage("Please use a valid number to pick a game! " + args[0] + " is not a number!")
                        );
                    }
                    if(type >= 0 && type <= GameType.class.getEnumConstants().length) {
                        invite(Arrays.copyOfRange(args, 1, args.length), event, GameType.values()[type]);
                    } else {
                        event.getTextChannel().sendMessage(MessageUtil
                                .errorMessage(type + " is not assigned to a game. Please use !help invite to get more information" +
                                "on which number represents which game!")).queue();
                    }
                } else {
                    event.getTextChannel().sendMessage(MessageUtil.errorMessage("Please specify a game and a Player to invite / challenge.")).queue();
                }
            } else {
                event.getTextChannel().sendMessage(MessageUtil.errorMessage("There is already a game running in this channel.")).queue();
            }
        }
    }

    private void invite(String[] args, MessageReceivedEvent event, GameType type) {
        if(args.length > 0) {
            String name = args[0];
            User user = JDAHelper.getUserByName(event.getJDA(), name, event);
            if(user != null && event.getGuild().isMember(user)) {
                if(!user.getId().equals(event.getAuthor().getId()) && !user.getId().equals(event.getJDA().getSelfUser().getId())) {
                    String gameName = "";
                    Game game = null;
                    switch(type) {
                        case CONNECT_FOUR: {
                            gameName = "Connect Four";
                            ConnectFour cf = new ConnectFour(event.getAuthor(), event.getTextChannel());
                            cf.registerPlayer2(user);
                            game = cf;
                        }
                    }
                    if(GameManager.getInstance().addGame(event.getTextChannel(), game))
                        event.getTextChannel().sendMessage(user.getAsMention() + " you have been challenged to a game of " + gameName + "!").queue();
                } else {
                    event.getTextChannel().sendMessage("You can't challenge me / yourself.");
                }
            } else {
                event.getTextChannel().sendMessage("You can only challenge members of `" + event.getGuild().getName() + "`").queue();
            }
        } else {
            event.getTextChannel().sendMessage("You need to specify a guild member to invite!").queue();
        }
    }
}
