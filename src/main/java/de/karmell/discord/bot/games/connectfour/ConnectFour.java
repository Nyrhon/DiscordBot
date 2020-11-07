package de.karmell.discord.bot.games.connectfour;

import de.karmell.discord.bot.games.Game;
import de.karmell.discord.bot.games.GameType;
import de.karmell.discord.bot.games.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.Random;

public class ConnectFour implements Game {
    private PlayerWrapper player1;
    private PlayerWrapper player2;
    private boolean started;
    private boolean won;
    private boolean tie;
    private int[][] playField;
    private int moveCount;
    private TextChannel txChannel;

    public ConnectFour(User u, TextChannel txChannel) {
        this.txChannel = txChannel;
        won = false;
        started = false;
        tie = false;
        moveCount = 0;
        player1 = new PlayerWrapper(new Color(94, 169, 222), u);
        playField = new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0}
        };
    }

    public void switchTurns() {
        player1.turn();
        player2.turn();
    }

    public boolean hasStarted() { return started; }

    public boolean isTurn(User u) {
        if(player1.getUser().getId().equals(u.getId())) {
            return player1.getTurn();
        } else {
            return player2.getTurn();
        }
    }

    private int currentTurnValue() { return isTurn(player1.getUser()) ? 1 : 2; }

    @Override
    public boolean hasPlayer(User user) {
        if((player1 != null && player1.getUser().getId().equals(user.getId()))
                || (player2 != null && player2.getUser().getId().equals(user.getId())))
            return true;
        return  false;
    }



    public void start() {
        started = true;
    }

    @Override
    public GameType type() {
        return GameType.CONNECT_FOUR;
    }

    @Override
    public User creator() {
        return player1.getUser();
    }

    @Override
    public TextChannel getTxChannel() {
        return txChannel;
    }

    public boolean move(int column) {
        for(int j = 5; j >= 0; j --) {
            if(playField[j][column - 1] == 0) {
                playField[j][column - 1] += currentTurnValue();
                won = checkIfWon(j, column - 1);
                tie = !(won) && checkForTie();
                switchTurns();
                moveCount ++;
                return true;
            }
        }
        return false;
    }

    private boolean checkIfWon(int i, int j) {
        int val = currentTurnValue();
        if(j+3 < 7 && playField[i][j] == val && playField[i][j+1] == val && playField[i][j+2] == val && playField[i][j+3] == val)
            return true;
        if(j+2 < 7 && j-1 >= 0 && playField[i][j-1] == val && playField[i][j] == val && playField[i][j+1] == val && playField[i][j+2] == val)
            return true;
        if(j+1 < 7 && j-2 >= 0 && playField[i][j-2] == val && playField[i][j-1] == val && playField[i][j] == val && playField[i][j+1] == val)
            return true;
        if(j < 7 && j-3 >= 0 && playField[i][j-3] == val && playField[i][j-2] == val && playField[i][j-1] == val && playField[i][j] == val)
            return true;
        if(j+1 < 7 && j-2 >= 0 && playField[i][j-2] == val && playField[i][j-1] == val && playField[i][j] == val && playField[i][j+1] == val)
            return true;
        if(i - 3 >= 0 && j - 3 >= 0 && playField[i-1][j-1] == val && playField[i-2][j-2] == val && playField[i-3][j-3] == val && playField[i][j] == val)
            return true;
        if(i - 2 >= 0 && j - 2 >= 0 && i + 1 < 6  && j + 1 < 7 && playField[i-1][j-1] == val && playField[i-2][j-2] == val && playField[i][j] == val && playField[i+1][j+1] == val)
            return true;
        if(i - 1 >= 0 && j - 1 >= 0 && i + 2 < 6  && j + 2 < 7 && playField[i-1][j-1] == val && playField[i][j] == val && playField[i+1][j+1] == val && playField[i+2][j+2] == val)
            return true;
        if(i >= 0 && j >= 0 && i + 3 < 6  && j + 3 < 7 && playField[i][j] == val && playField[i+1][j+1] == val && playField[i+2][j+2] == val && playField[i+3][j+3] == val)
            return true;
        if(i + 3 < 6 && j - 3 >= 0 && playField[i+1][j-1] == val && playField[i+2][j-2] == val && playField[i+3][j-3] == val && playField[i][j] == val)
            return true;
        if(i + 2 < 6 && j - 2 >= 0 && i - 1 >= 0 && j + 1 < 7 && playField[i+1][j-1] == val && playField[i+2][j-2] == val && playField[i-1][j+1] == val && playField[i][j] == val)
            return true;
        if(i + 1 < 6 && j - 1 >= 0 && i - 2 >= 0 && j + 2 < 7 && playField[i+1][j-1] == val && playField[i-2][j+2] == val && playField[i-1][j+1] == val && playField[i][j] == val)
            return true;
        if(i + 3 < 6 && playField[i][j] == val && playField[i+1][j] == val && playField[i+2][j] == val && playField[i+3][j] == val)
            return true;
        if(i + 2 < 6 && i - 1 >= 0 && playField[i-1][j] == val && playField[i][j] == val && playField[i+1][j] == val && playField[i+2][j] == val)
            return true;
        if(i + 1 < 6 && i - 2 >= 0 && playField[i-2][j] == val && playField[i-1][j] == val && playField[i][j] == val && playField[i+1][j] == val)
            return true;
        if(i - 3 >= 0 && playField[i-3][j] == val && playField[i-2][j] == val && playField[i-1][j] == val && playField[i][j] == val)
            return true;
        return false;
    }

    private boolean checkForTie() {
        return moveCount == 42;
    }

    public boolean terminated() {
        return won || tie;
    }

    public EmbedBuilder getPlayFieldAsEmbed() {
        EmbedBuilder eb = new EmbedBuilder();
        StringBuilder description = new StringBuilder();
        for (int[] ints : playField) {
            StringBuilder row = new StringBuilder();
            for (int anInt : ints) {
                if (anInt == 0) {
                    row.append(":white_circle: ");
                } else if (anInt == 1) {
                    row.append(":blue_circle: ");
                } else if (anInt == 2) {
                    row.append(":red_circle: ");
                }
            }
            description.append(row).append("\n");
        }
        eb.setDescription(description.toString());
        if(isTurn(player1.getUser())) {
            setTitleAndColor(eb, player2, player1);
        } else {
            setTitleAndColor(eb, player1, player2);
        }
        return eb;
    }

    private void setTitleAndColor(EmbedBuilder e, PlayerWrapper playerWon, PlayerWrapper playerTurn) {
        if(won) {
            e.setTitle(playerWon.getUser().getName() + " you have won the game!");
            e.setColor(playerWon.c);
        } else if(tie) {
            e.setTitle("It's a tie!");
            e.setColor(Color.ORANGE);
        } else {
            e.setTitle(playerTurn.getUser().getName() + " it's now your turn!");
            e.setColor(playerTurn.c);
        }
    }

    public void registerPlayer2(User u) {
        player2 = new PlayerWrapper(new Color(222, 46, 68), u);
        if(new Random().nextInt(100) % 2 == 0) {
            player1.turn();
        } else {
            player2.turn();
        }
    }

    private class PlayerWrapper extends Player {
        private Color c;

        private PlayerWrapper(Color c, User u) {
            super(u);
            this.c = c;
        }
    }
}
