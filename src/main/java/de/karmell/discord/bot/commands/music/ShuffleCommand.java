package de.karmell.discord.bot.commands.music;

import de.karmell.discord.bot.Main;
import de.karmell.discord.bot.audio.GuildAudioManager;
import de.karmell.discord.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class ShuffleCommand implements Command {
    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(event.getChannel().getType().isGuild()) {
            GuildAudioManager manager = Main.GUILD_MUSIC_MANAGERS.get(event.getGuild().getId());
            if(manager != null && manager.getMessageChannel().getId().equals(event.getChannel().getId())) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.ORANGE);
                if(manager.isShuffling()) {
                    embed.addField("", "Stopped shuffle playback.", false);
                } else {
                    embed.addField("", "Started shuffle playback.", false);
                }
                manager.setShuffle(!manager.isShuffling());
                event.getChannel().sendMessage(embed.build()).queue();
            }
        }
    }

    @Override
    public String describe() {
        return "Shuffles all currently queued songs";
    }
}
