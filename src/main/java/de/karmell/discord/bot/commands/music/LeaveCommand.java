package de.karmell.discord.bot.commands.music;

import de.karmell.discord.bot.Main;
import de.karmell.discord.bot.audio.GuildAudioManager;
import de.karmell.discord.bot.commands.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class LeaveCommand implements Command {
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(event.getChannel().getType().isGuild()) {
            GuildAudioManager manager = Main.GUILD_MUSIC_MANAGERS.get(event.getGuild().getId());
            if(manager != null && manager.getMessageChannel().getId().equals(event.getChannel().getId())) {
                manager.getQueue().clear();
                manager.getPlayer().stopTrack();
                manager.getPlayer().setPaused(false);
                event.getGuild().getAudioManager().setSendingHandler(null);
                event.getGuild().getAudioManager().closeAudioConnection();
                Main.GUILD_MUSIC_MANAGERS.remove(event.getGuild().getId());
            }
        }
    }

    public String describe() {
        return "Tells the bot to leave the voice channel of the guild.";
    }
}
