package de.karmell.discord.bot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import de.karmell.discord.bot.audio.GuildAudioManager;
import de.karmell.discord.bot.commands.games.AcceptCommand;
import de.karmell.discord.bot.commands.games.CancelCommand;
import de.karmell.discord.bot.commands.games.InviteCommand;
import de.karmell.discord.bot.commands.games.MoveCommand;
import de.karmell.discord.bot.commands.general.HelpCommand;
import de.karmell.discord.bot.commands.general.PurgeCommand;
import de.karmell.discord.bot.commands.general.StopCommand;
import de.karmell.discord.bot.commands.music.*;
import de.karmell.discord.bot.commands.CommandManager;
import de.karmell.discord.bot.util.GuildWrapper;
import de.karmell.discord.bot.listeners.GuildJoinedListener;
import de.karmell.discord.bot.listeners.ReadyListener;
import de.karmell.discord.bot.util.Config;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.hooks.AnnotatedEventManager;

import javax.security.auth.login.LoginException;
import java.util.HashMap;

// invite link https://discordapp.com/oauth2/authorize?client_id=503216787903479810&scope=bot&permissions=0

public class Bot {
    private static Config config;
    private static CommandManager commandManager;
    private static AudioPlayerManager audioPlayerManager;
    private static HashMap<String, GuildAudioManager> guildAudioManagers;
    private static HashMap<String, GuildWrapper> joinedGuilds;

    public static void main(String[] args) {
        audioPlayerManager = new DefaultAudioPlayerManager();
        audioPlayerManager.registerSourceManager(new YoutubeAudioSourceManager());
        config = new Config();
        commandManager = new CommandManager(config.COMMAND_PREFIX);
        registerCommands();
        guildAudioManagers = new HashMap<>();
        joinedGuilds = new HashMap<>();

        JDABuilder jda = new JDABuilder(AccountType.BOT);
        jda.setToken(config.BOT_TOKEN);
        jda.setAutoReconnect(true);
        jda.setStatus(OnlineStatus.ONLINE);
        jda.setGame(Game.of(Game.GameType.DEFAULT, config.GAME));
        AnnotatedEventManager eventManager = new AnnotatedEventManager();
        eventManager.register(commandManager);
        eventManager.register(new ReadyListener());
        eventManager.register(new GuildJoinedListener());
        jda.setEventManager(eventManager);
        try {
            jda.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    private static void registerCommands() {
        commandManager.registerCommands(
                new StopCommand(),
                new HelpCommand(),
                new PurgeCommand(),
                new ClearCommand(),
                new CurrentSongCommand(),
                new LeaveCommand(),
                new LoopCommand(),
                new PauseCommand(),
                new PlayCommand(),
                new QueueCommand(),
                new SkipCommand(),
                new ShuffleCommand(),
                new MoveCommand(),
                new AcceptCommand(),
                new CancelCommand(),
                new InviteCommand()
        );
    }

    public static Config getConfig() {
        return config;
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }

    public static AudioPlayerManager getAudioPlayerManager() {
        return audioPlayerManager;
    }

    public static HashMap<String, GuildWrapper> getJoinedGuilds() { return joinedGuilds; }

    public static HashMap<String, GuildAudioManager> getGuildAudioManagers() { return guildAudioManagers; }
}
