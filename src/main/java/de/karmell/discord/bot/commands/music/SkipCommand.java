package de.karmell.discord.bot.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.karmell.discord.bot.Main;
import de.karmell.discord.bot.audio.GuildAudioManager;
import de.karmell.discord.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class SkipCommand implements Command {
    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(event.getChannel().getType().isGuild()) {
            GuildAudioManager manager = Main.GUILD_MUSIC_MANAGERS.get(event.getGuild().getId());
            if(manager != null && manager.getMessageChannel().getId().equals(event.getChannel().getId())) {
                if(args.length > 0) {
                    Integer skipAmt = null;
                    try {
                        skipAmt = Integer.parseInt(args[0]);
                    } catch (NumberFormatException e) {
                        event.getChannel().sendMessage("Specified argument was not a number.").queue();
                    }
                    if(skipAmt != null) {
                        if(skipAmt < 1 || skipAmt > manager.getQueue().size()) {
                            event.getChannel().sendMessage("Specified number is out of range.").queue();
                        } else {
                            for(int i = 0; i < skipAmt; i++) {
                                manager.skip();
                            }
                            manager.getPlayer().setPaused(false);
                            AudioTrack track = manager.getPlayer().getPlayingTrack();
                            EmbedBuilder embed = new EmbedBuilder();
                            embed.setColor(Color.ORANGE);
                            embed.addField("Now playing", "[" + track.getInfo().title + "](" +
                                    track.getInfo().uri +")", false);
                            event.getChannel().sendMessage(embed.build()).queue();
                        }
                    }
                } else {
                    manager.skip();
                    AudioTrack track = manager.getPlayer().getPlayingTrack();
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(Color.ORANGE);
                    embed.addField("Now playing", "[" + track.getInfo().title + "](" +
                            track.getInfo().uri +")", false);
                    event.getChannel().sendMessage(embed.build()).queue();
                }
            }
        }
    }

    @Override
    public String describe() {
        return "Skips the current song.";
    }
}
