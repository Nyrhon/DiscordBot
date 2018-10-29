package de.karmell.discord.bot.core;

import de.karmell.discord.bot.Bot;
import de.karmell.discord.bot.util.MessageUtil;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private Map<String, Command> commands;
    private final String prefix;
    private final Logger log;

    public CommandManager(String prefix) {
        commands = new HashMap<>();
        this.prefix = prefix;
        log = LogManager.getLogger(CommandManager.class);
    }

    @SubscribeEvent
    private void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();
        if(!author.isBot() && !author.isFake() && !event.isWebhookMessage()) {
            String contentRaw = event.getMessage().getContentRaw();
            if(contentRaw.startsWith(prefix)) {
                String invoke = contentRaw.substring(prefix.length(), contentRaw.contains(" ") ? contentRaw.indexOf(" ") : contentRaw.length());
                String[] args = contentRaw.contains(" ") ? contentRaw.substring(contentRaw.indexOf(" ") + 1).split(" ") : new String[0];
                Command command = commands.get(invoke);
                if(command == null) {
                    event.getChannel().sendMessage(MessageUtil.errorMessage("Unknown command.")).queue();
                } else {
                    command.invoke(args, event);
                }
            }
        }
    }

    public void registerCommands(Command... commands) {
        for(Command c : commands) {
            registerCommand(c);
        }
    }

    public void registerCommand(Command command) {
        for(String alias : command.getAliases()) {
            if(commands.containsKey(alias)) {
                log.warn("Command alias " + alias + "already in use.");
            } else {
                commands.put(alias, command);
            }
        }
    }

    public Command getCommand(String alias) {
        return commands.get(alias);
    }

    public Map<String, Command> getCommands() { return commands; }
}
