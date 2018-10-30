package de.karmell.discord.bot.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import de.karmell.discord.bot.Bot;
import de.karmell.discord.bot.audio.GuildAudioManager;
import de.karmell.discord.bot.commands.Command;
import de.karmell.discord.bot.util.MessageUtil;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Shows information about the currently playing song.
 */
public class CurrentSongCommand extends Command {
    public CurrentSongCommand() {
        super(new String[]{"current"}, CommandCategory.MUSIC, "Shows information for the current song.");
    }

    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(event.getChannel().getType().isGuild()) {
            GuildAudioManager manager = Bot.getGuildAudioManagers().get(event.getGuild().getId());
            if(manager != null && manager.getMessageChannel().getId().equals(event.getChannel().getId())) {
                AudioTrackInfo info = manager.getPlayer().getPlayingTrack().getInfo();
                event.getChannel().sendMessage(MessageUtil.songInfo("Currently playing", info)).queue();
            }
        }
    }
}
