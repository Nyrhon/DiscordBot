package de.karmell.discord.bot.games;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public interface Game {
    boolean hasStarted();
    boolean hasPlayer(User user);
    boolean isTurn(User user);
    GameType type();
    User creator();
    TextChannel getTxChannel();
}
