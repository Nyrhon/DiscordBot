package de.karmell.discord.bot.games;

import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameManager {
    private static GameManager instance;
    private static HashMap<String, Game> games;

    private GameManager() {
        games = new HashMap<>();
    }

    public static GameManager getInstance() {
        if(instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public boolean addGame(TextChannel t, Game g) {
        if(!games.containsKey(t.getId())) {
            games.put(t.getId(), g);
            return true;
        } else {
            return false;
        }
    }

    public void removeGame(TextChannel t) {
        if(games.containsKey(t.getId())) {
            games.remove(t.getId());
        }
    }

    public Game getGame(TextChannel t) {
        return games.get(t.getId());
    }

    public List<Game> getAllGames() {
        List<Game> result = new ArrayList<>();
        games.forEach((channelId, game) -> result.add(game));
        return result;
    }
}
