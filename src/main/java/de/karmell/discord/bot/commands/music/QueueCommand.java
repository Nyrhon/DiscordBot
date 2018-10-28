package de.karmell.discord.bot.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.karmell.discord.bot.Main;
import de.karmell.discord.bot.audio.GuildAudioManager;
import de.karmell.discord.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Queue;

public class QueueCommand implements Command {
    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(event.getChannel().getType().isGuild()) {
            GuildAudioManager manager = Main.GUILD_MUSIC_MANAGERS.get(event.getGuild().getId());
            if(manager != null && manager.getMessageChannel().getId().equals(event.getChannel().getId())) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(Color.ORANGE);
                StringBuilder builder = new StringBuilder();
                Queue<AudioTrack> tracks = manager.getQueue();
                ArrayList<AudioTrack> itrTracks = new ArrayList<>(tracks);
                for(int i = 0; i < itrTracks.size() && i < 10; i++) {
                    builder.append("`" + (i+1) + ":` " + itrTracks.get(i).getInfo().title + "\n");
                }
                if(itrTracks.size() > 10) {
                    builder.append("And " + (itrTracks.size() - 10) + " more in queue");
                }
                if(itrTracks.size() == 0) {
                    builder.append("There are no songs in the queue right now.");
                }
                event.getChannel().sendMessage(embedBuilder.addField("Queued songs", builder.toString(), false).build()).queue();
            }
        }
    }

    @Override
    public String describe() {
        return "Returns a list of the currently queued songs.";
    }
}
