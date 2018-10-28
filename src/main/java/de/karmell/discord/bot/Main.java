package de.karmell.discord.bot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import de.karmell.discord.bot.audio.GuildAudioManager;
import de.karmell.discord.bot.commands.*;
import de.karmell.discord.bot.commands.music.*;
import de.karmell.discord.bot.listeners.MessageReceivedListener;
import de.karmell.discord.bot.util.Config;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.List;

// invite link https://discordapp.com/oauth2/authorize?client_id=503216787903479810&scope=bot&permissions=0

public class Main {
    private static JDABuilder jda;
    public static final Config CONFIG = new Config();
    public static final HashMap<String, Command> COMMANDS = new HashMap<>() {{
        put("help", new HelpCommand());
        put("stop", new StopCommand());
        put("play", new PlayCommand());
        put("leave", new LeaveCommand());
        put("clear", new ClearCommand());
        put("skip", new SkipCommand());
        put("queue", new QueueCommand());
        put("current", new CurrentSongCommand());
        put("shuffle", new ShuffleCommand());
        put("purge", new PurgeCommand());
        put("pause", new PauseCommand());
        put("loop", new LoopCommand());
    }};
    public static final AudioPlayerManager AUDIO_PLAYER_MANAGER = new DefaultAudioPlayerManager();
    public static HashMap<String, GuildAudioManager> GUILD_MUSIC_MANAGERS = new HashMap<>();
    public static HashMap<String, List<String>> BOT_OWNED_CHANNELS = new HashMap<>() {{
        put("272358210193719296", List.of("505062232233607168"));
    }};


    public static void main(String[] args) {
        AUDIO_PLAYER_MANAGER.registerSourceManager(new YoutubeAudioSourceManager());

        jda = new JDABuilder(AccountType.BOT);

        jda.setToken(CONFIG.BOT_TOKEN);
        jda.setAutoReconnect(true);
        jda.setStatus(OnlineStatus.ONLINE);
        jda.setGame(Game.of(Game.GameType.DEFAULT, CONFIG.GAME));
        jda.addEventListener(new MessageReceivedListener());

        try {
            jda.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
}
