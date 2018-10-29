package de.karmell.discord.bot.commands.music;

import de.karmell.discord.bot.Bot;
import de.karmell.discord.bot.audio.GuildAudioManager;
import de.karmell.discord.bot.core.Command;
import de.karmell.discord.bot.util.MessageUtil;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Clears the current queue.
 */
public class ClearCommand extends Command {
    public ClearCommand() {
        super(new String[] {"clear"}, CommandCategory.MUSIC, "Clears the current queue.");
    }

    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(event.getChannel().getType().isGuild()) {
            GuildAudioManager manager = Bot.getGuildAudioManagers().get(event.getGuild().getId());
            if(manager != null && manager.getMessageChannel().getId().equals(event.getChannel().getId())) {
                manager.getQueue().clear();
                manager.getPlayer().stopTrack();
                event.getChannel().sendMessage(MessageUtil.simpleMessage("Playlist has been cleared.")).queue();
            }
        }
    }
}
