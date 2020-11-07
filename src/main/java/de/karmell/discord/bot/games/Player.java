package de.karmell.discord.bot.games;

import net.dv8tion.jda.api.entities.User;

public class Player {
    private User u;
    private boolean turn;

    public Player(User u) {
        this.u = u;
        turn = false;
    }

    public User getUser() { return u; }
    public void turn() { turn = !turn; }
    public boolean getTurn() { return turn; }
}
