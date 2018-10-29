package de.karmell.discord.bot.commands.music;

import de.karmell.discord.bot.Bot;
import de.karmell.discord.bot.audio.GuildAudioManager;
import de.karmell.discord.bot.core.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class LeaveCommand extends Command {
    public LeaveCommand() {
        super(new String[]{"leave"}, CommandCategory.MUSIC, "Makes the bot leave the voice channel and clear the queue.");
    }

    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(event.getChannel().getType().isGuild()) {
            GuildAudioManager manager = Bot.getGuildAudioManagers().get(event.getGuild().getId());
            if(manager != null && manager.getMessageChannel().getId().equals(event.getChannel().getId())) {
                manager.getQueue().clear();
                manager.getPlayer().stopTrack();
                manager.getPlayer().setPaused(false);
                event.getGuild().getAudioManager().setSendingHandler(null);
                event.getGuild().getAudioManager().closeAudioConnection();
                Bot.getGuildAudioManagers().remove(event.getGuild().getId());
            }
        }
    }
}
