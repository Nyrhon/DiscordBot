package de.karmell.discord.bot.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.karmell.discord.bot.Bot;
import de.karmell.discord.bot.audio.GuildAudioManager;
import de.karmell.discord.bot.core.Command;
import de.karmell.discord.bot.util.MessageUtil;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

/**
 * Adds song / playlist to the queue or resumes playback.
 */
public class PlayCommand extends Command {
    public PlayCommand() {
        super(new String[]{"play", "p"}, CommandCategory.MUSIC, "Plays the specified song / playlist or resumes playback.");
    }

    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(event.getChannel().getType().isGuild()) {
            GuildAudioManager manager = Bot.getGuildAudioManagers().get(event.getGuild().getId());
            boolean loadAndPlay = false; // to determine if preconditions to play music have been met
            if(manager == null) {
                // create new GuildAudioManager for current guild and determine the voice channel of person
                // who called the command
                manager = new GuildAudioManager(Bot.getAudioPlayerManager());
                manager.getPlayer().setVolume(100);
                Bot.getGuildAudioManagers().put(event.getGuild().getId(), manager);
                List<VoiceChannel> channels = event.getGuild().getVoiceChannels();
                VoiceChannel channelToJoin = null;
                for(VoiceChannel channel : channels) {
                    List<Member> members = channel.getMembers();
                    for(Member member : members) {
                        if(member.getUser().getId().equals(event.getAuthor().getId())) {
                            channelToJoin = channel;
                        }
                    }
                }
                if(channelToJoin == null) {
                    event.getChannel().sendMessage(
                            MessageUtil.errorMessage(event.getAuthor().getAsMention() + " please join a voice channel before using the command.")
                    ).queue();
                } else {
                    event.getGuild().getAudioManager().setSendingHandler(manager.getSendHandler());
                    event.getGuild().getAudioManager().openAudioConnection(channelToJoin);
                    loadAndPlay = true;
                    manager.setMessageChannel(event.getChannel());
                    manager.setGuild(event.getGuild());
                    event.getChannel().sendMessage(MessageUtil.simpleMessage("Joining voice channel **" + channelToJoin.getName()
                            + "** and bound music commands and notifications to **" + event.getChannel().getName() + "**")).queue();
                }
            } else {
                loadAndPlay = true;
            }
            if(loadAndPlay && args.length > 0 && manager.getMessageChannel().getId().equals(event.getChannel().getId())) {
                if(args[0].startsWith("http")) { // arg has to be an url so we simply query for the url
                    addToQueue(manager, event.getChannel(), args[0], true);
                    manager.getPlayer().setPaused(false);
                } else { // search for given args
                    StringBuilder query = new StringBuilder();
                    query.append("ytsearch:");
                    for(String s : args) {
                        query.append(s + " ");
                    }
                    addToQueue(manager, event.getChannel(), query.toString(),false);
                    manager.getPlayer().setPaused(false);
                }
            }
            // if currently paused resume playback no matter how the command has been called
            if(manager.getPlayer().isPaused() && manager.getMessageChannel().getId().equals(event.getChannel().getId())) {
                event.getChannel().sendMessage(MessageUtil.simpleMessage("Resuming playback.")).queue();
                manager.getPlayer().setPaused(false);
            }
        }
    }

    /**
     * Adds the specified song / playlist to the queue or adds the first search result to the queue.
     * @param guildAudioManager AudioManager of the current guild
     * @param channel MessageChannel the music control is bound to
     * @param uri Either url or search term for the YoutubeAudioSourceManager
     * @param playlist Whether or not to add a whole playlist (if found e.g. for searches)
     */
    public static void addToQueue(final GuildAudioManager guildAudioManager, final MessageChannel channel, String uri, boolean playlist) {
        Bot.getAudioPlayerManager().loadItemOrdered(guildAudioManager, uri, new AudioLoadResultHandler()
        {
            public void trackLoaded(AudioTrack track)
            {
                guildAudioManager.queue(track);
                channel.sendMessage(MessageUtil.songInfo("Added to queue", track.getInfo())).queue();
            }

            public void playlistLoaded(AudioPlaylist aPlaylist)
            {
                AudioTrack firstTrack = aPlaylist.getSelectedTrack();
                List<AudioTrack> tracks = aPlaylist.getTracks();

                if (firstTrack == null) {
                    firstTrack = aPlaylist.getTracks().get(0);
                }

                if(playlist) {
                    tracks.forEach(guildAudioManager::queue);
                    channel.sendMessage(MessageUtil.simpleMessage("Added to queue", "Added all *" + aPlaylist.getTracks().size() +
                            "* songs from playlist " + aPlaylist.getName() + " to the queue.")).queue();
                }
                else {
                    guildAudioManager.queue(firstTrack);
                    channel.sendMessage(MessageUtil.songInfo("Added to queue", firstTrack.getInfo())).queue();
                }
            }

            public void noMatches()
            {
                channel.sendMessage(MessageUtil.errorMessage(uri + " not found.")).queue();
            }

            public void loadFailed(FriendlyException exception) { }
        });
    }
}
