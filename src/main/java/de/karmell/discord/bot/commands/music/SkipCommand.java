package de.karmell.discord.bot.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.karmell.discord.bot.Bot;
import de.karmell.discord.bot.audio.GuildAudioManager;
import de.karmell.discord.bot.commands.Command;
import de.karmell.discord.bot.util.MessageUtil;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


/**
 * Skips the current song.
 */
public class SkipCommand extends Command {
    public SkipCommand() {
        super(new String[]{"skip", "s", "next"}, CommandCategory.MUSIC, "Skips the current song.");
    }

    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if (event.getChannel().getType().isGuild()) {
            GuildAudioManager manager = Bot.getGuildAudioManagers().get(event.getGuild().getId());
            if (manager != null && manager.getMessageChannel().getId().equals(event.getChannel().getId())) {
                if (args.length > 0) {
                    Integer skipAmt = null;
                    try {
                        skipAmt = Integer.parseInt(args[0]);
                    } catch (NumberFormatException e) {
                        event.getChannel().sendMessage("Specified argument was not a number.").queue();
                    }
                    if (skipAmt != null) {
                        if (skipAmt < 1 || skipAmt > manager.getQueue().size()) {
                            event.getChannel().sendMessage("Specified number is out of range.").queue();
                        } else {
                            for (int i = 0; i < skipAmt; i++) {
                                manager.skip();
                            }
                            manager.getPlayer().setPaused(false);
                            AudioTrack track = manager.getPlayer().getPlayingTrack();
                            event.getChannel().sendMessage(MessageUtil.songInfo("Now playing", track.getInfo())).queue();
                        }
                    }
                } else {
                    manager.skip();
                }
            }
        }
    }
}
