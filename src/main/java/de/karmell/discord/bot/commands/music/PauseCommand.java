package de.karmell.discord.bot.commands.music;

import de.karmell.discord.bot.Main;
import de.karmell.discord.bot.audio.GuildAudioManager;
import de.karmell.discord.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class PauseCommand implements Command {
    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(event.getChannelType().isGuild()) {
            GuildAudioManager manager = Main.GUILD_MUSIC_MANAGERS.get(event.getGuild().getId());
            if(manager != null && manager.getMessageChannel().getId().equals(event.getChannel().getId())) {
                manager.getPlayer().setPaused(true);
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.ORANGE);
                embed.addField("", "Playback has been paused.", false);
                event.getChannel().sendMessage(embed.build()).queue();
            }
        }
    }

    @Override
    public String describe() {
        return "Pauses music playback.";
    }
}
