package de.karmell.discord.bot.listeners;

import de.karmell.discord.bot.Main;
import de.karmell.discord.bot.commands.Command;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MessageReceivedListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        MessageChannel channel = event.getChannel();
        if(message.startsWith(Main.CONFIG.COMMAND_PREFIX)) {
            String invoke = message.substring(1, message.contains(" ") ? message.indexOf(" ") : message.length());
            String[] args = message.contains(" ") ? message.substring(message.indexOf(" ") + 1).split(" ") : new String[0];
            Command command = Main.COMMANDS.get(invoke);
            if(command == null) {
                channel.sendMessage("Unknown command.");
            } else {
                command.invoke(args, event);
            }
        }
        List<String> botOwnedChannels = Main.BOT_OWNED_CHANNELS.get(event.getGuild().getId());
        if(botOwnedChannels != null && botOwnedChannels.contains(channel.getId())) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    event.getTextChannel().deleteMessageById(event.getMessageId()).queue();
                }
            }, 10000);
        }
    }
}
