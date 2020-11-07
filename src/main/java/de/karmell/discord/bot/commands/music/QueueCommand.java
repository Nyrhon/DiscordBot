package de.karmell.discord.bot.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.karmell.discord.bot.Bot;
import de.karmell.discord.bot.audio.GuildAudioManager;
import de.karmell.discord.bot.commands.Command;
import de.karmell.discord.bot.util.MessageUtil;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Queue;

/**
 * Shows information about the upcoming songs
 */
public class QueueCommand extends Command {
    public QueueCommand() {
        super(new String[]{"queue" , "q" ,"playlist"}, CommandCategory.MUSIC, "Shows a list of upcoming songs.");
    }

    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if (event.getChannel().getType().isGuild()) {
            GuildAudioManager manager = Bot.getGuildAudioManagers().get(event.getGuild().getId());
            if (manager != null && manager.getMessageChannel().getId().equals(event.getChannel().getId())) {
                if(manager.isShuffling()) {
                    event.getChannel().sendMessage(MessageUtil
                            .simpleMessage("Currently in shuffle mode so you are going to get surprised on what's up next.")).queue();
                } else {
                    StringBuilder builder = new StringBuilder();
                    Queue<AudioTrack> tracks = manager.getQueue();
                    ArrayList<AudioTrack> itrTracks = new ArrayList<>(tracks);
                    for (int i = 0; i < itrTracks.size() && i < 10; i++) {
                        builder.append("`" + (i + 1) + ":` " + itrTracks.get(i).getInfo().title + "\n");
                    }
                    if (itrTracks.size() > 10) {
                        builder.append("And " + (itrTracks.size() - 10) + " more in queue");
                    }
                    if (itrTracks.size() == 0) {
                        builder.append("There are no songs in the queue right now.");
                    }
                    event.getChannel().sendMessage(MessageUtil.simpleMessage("Queued songs", builder.toString())).queue();
                }
            }
        }
    }
}
