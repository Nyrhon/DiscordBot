package de.karmell.discord.bot.commands.music;

import de.karmell.discord.bot.Main;
import de.karmell.discord.bot.audio.GuildAudioManager;
import de.karmell.discord.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.Color;

public class LoopCommand implements Command {
    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(event.getChannelType().isGuild()) {
            GuildAudioManager manager = Main.GUILD_MUSIC_MANAGERS.get(event.getGuild().getId());
            if(manager != null && manager.getMessageChannel().getId().equals(event.getChannel().getId())) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.ORANGE);
                if(manager.isLoop()) {
                    embed.addField("Started loop", "Looping has been stopped. Resuming to normal queue.", false);
                } else {
                    embed.addField("Stopped loop", "Now looping [" + manager.getPlayer().getPlayingTrack().getInfo().title +
                            "](" + manager.getPlayer().getPlayingTrack().getInfo().uri + ").", false);
                }
                manager.setLoop(!manager.isLoop());
                event.getChannel().sendMessage(embed.build()).queue();
            }
        }
    }

    @Override
    public String describe() {
        return "Starts / stops looping the playback of the current song.";
    }
}
