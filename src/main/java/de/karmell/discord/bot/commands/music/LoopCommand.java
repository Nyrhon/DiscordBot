package de.karmell.discord.bot.commands.music;

import de.karmell.discord.bot.Bot;
import de.karmell.discord.bot.audio.GuildAudioManager;
import de.karmell.discord.bot.commands.Command;
import de.karmell.discord.bot.util.MessageUtil;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Starts / stops looping of the current song.
 */
public class LoopCommand extends Command {
    public LoopCommand() {
        super(new String[]{"loop"}, CommandCategory.MUSIC, "Makes the bot loop the current song.");
    }

    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(event.getChannelType().isGuild()) {
            GuildAudioManager manager = Bot.getGuildAudioManagers().get(event.getGuild().getId());
            if(manager != null && manager.getMessageChannel().getId().equals(event.getChannel().getId())) {
                if(manager.isLoop()) {
                    event.getChannel().sendMessage(MessageUtil.simpleMessage("Looping has been stopped. Resuming to normal queue.")).queue();
                } else {
                    event.getChannel().sendMessage(MessageUtil.songInfo("Now looping", manager.getPlayer().getPlayingTrack().getInfo())).queue();
                }
                manager.setLoop(!manager.isLoop());
            }
        }
    }
}
