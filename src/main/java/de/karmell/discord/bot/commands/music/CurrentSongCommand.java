package de.karmell.discord.bot.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import de.karmell.discord.bot.Main;
import de.karmell.discord.bot.audio.GuildAudioManager;
import de.karmell.discord.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class CurrentSongCommand implements Command {
    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(event.getChannel().getType().isGuild()) {
            GuildAudioManager manager = Main.GUILD_MUSIC_MANAGERS.get(event.getGuild().getId());
            if(manager != null && manager.getMessageChannel().getId().equals(event.getChannel().getId())) {
                AudioTrackInfo info = manager.getPlayer().getPlayingTrack().getInfo();
                EmbedBuilder builder = new EmbedBuilder();
                builder.setColor(Color.ORANGE);
                Duration d = Duration.of(info.length, ChronoUnit.MILLIS);
                builder.addField("Currently playing", "`Title:` " + info.title + "\n`Duration:` " +
                        String.format("%d:%02d%n", d.toMinutes(), d.minusMinutes(d.toMinutes()).getSeconds()), false);
                event.getChannel().sendMessage(builder.build()).queue();
                event.getChannel().sendMessage(info.uri).queue();
            }
        }
    }

    @Override
    public String describe() {
        return "Returns information for the current song.";
    }
}
