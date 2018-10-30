package de.karmell.discord.bot.commands.music;

import de.karmell.discord.bot.Bot;
import de.karmell.discord.bot.audio.GuildAudioManager;
import de.karmell.discord.bot.commands.Command;
import de.karmell.discord.bot.util.MessageUtil;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Pauses playback of the current song.
 */
public class PauseCommand extends Command {
    public PauseCommand() {
        super(new String[]{"pause"}, CommandCategory.MUSIC, "Pauses the music playback.");
    }

    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(event.getChannelType().isGuild()) {
            GuildAudioManager manager = Bot.getGuildAudioManagers().get(event.getGuild().getId());
            if(manager != null && manager.getMessageChannel().getId().equals(event.getChannel().getId())) {
                manager.getPlayer().setPaused(true);
                event.getChannel().sendMessage(MessageUtil.simpleMessage("Playback has been paused.")).queue();
            }
        }
    }
}
