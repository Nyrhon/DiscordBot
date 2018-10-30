package de.karmell.discord.bot.commands.music;

import de.karmell.discord.bot.Bot;
import de.karmell.discord.bot.audio.GuildAudioManager;
import de.karmell.discord.bot.commands.Command;
import de.karmell.discord.bot.util.MessageUtil;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Starts / stops shuffling mode.
 */
public class ShuffleCommand extends Command {
    public ShuffleCommand() {
        super(new String[]{"shuffle"}, CommandCategory.MUSIC, "Starts shuffle playback.");
    }

    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(event.getChannel().getType().isGuild()) {
            GuildAudioManager manager = Bot.getGuildAudioManagers().get(event.getGuild().getId());
            if(manager != null && manager.getMessageChannel().getId().equals(event.getChannel().getId())) {
                if(manager.isShuffling()) {
                    event.getChannel().sendMessage(MessageUtil.simpleMessage("Stopping shuffle playback.")).queue();
                } else {
                    event.getChannel().sendMessage(MessageUtil.simpleMessage("Starting shuffle playback.")).queue();
                }
                manager.setShuffle(!manager.isShuffling());
            }
        }
    }
}
