package de.karmell.discord.bot.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.karmell.discord.bot.Main;
import de.karmell.discord.bot.audio.GuildAudioManager;
import de.karmell.discord.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class PlayCommand implements Command {
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(event.getChannel().getType().isGuild()) {
            GuildAudioManager manager = Main.GUILD_MUSIC_MANAGERS.get(event.getGuild().getId());
            boolean loadAndPlay = false;
            if(manager == null) {
                manager = new GuildAudioManager(Main.AUDIO_PLAYER_MANAGER);
                manager.getPlayer().setVolume(100);
                Main.GUILD_MUSIC_MANAGERS.put(event.getGuild().getId(), manager);
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
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + " please join a voice channel before using the command.");
                } else {
                    event.getGuild().getAudioManager().setSendingHandler(manager.getSendHandler());
                    event.getGuild().getAudioManager().openAudioConnection(channelToJoin);
                    loadAndPlay = true;
                    manager.setMessageChannel(event.getChannel());
                    manager.setGuild(event.getGuild());
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(Color.ORANGE);
                    embed.addField("", "Joining voice channel " + channelToJoin.getName() +
                            " and bound commands and notifications to " + event.getChannel().getName(), false);
                    event.getChannel().sendMessage(embed.build());
                }
            } else {
                loadAndPlay = true;
            }
            if(loadAndPlay && args.length > 0 && manager.getMessageChannel().getId().equals(event.getChannel().getId())) {
                if(args[0].startsWith("http")) {
                    addToQueue(manager, event.getChannel(), args[0], true);
                } else {
                    StringBuilder query = new StringBuilder();
                    query.append("ytsearch:");
                    for(String s : args) {
                        query.append(s + " ");
                    }
                    addToQueue(manager, event.getChannel(), query.toString(),false);
                }
            }
            if(manager.getPlayer().isPaused() && manager.getMessageChannel().getId().equals(event.getChannel().getId())) {
                manager.getPlayer().setPaused(false);
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.ORANGE);
                embed.addField("", "Resumed playback.", false);
                event.getChannel().sendMessage(embed.build()).queue();
            }
        }
    }

    public String describe() {
        return "Adds given description / youtube link to the queue.";
    }

    public static void addToQueue(final GuildAudioManager guildAudioManager, final MessageChannel channel, String uri, boolean playlist) {
        Main.AUDIO_PLAYER_MANAGER.loadItemOrdered(guildAudioManager, uri, new AudioLoadResultHandler()
        {
            public void trackLoaded(AudioTrack track)
            {
                guildAudioManager.queue(track);
                sendSongAddedMsg(track, channel);
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
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(Color.ORANGE);
                    embed.addField("Playlist added", "Added all *" + aPlaylist.getTracks().size() +
                            "* songs from playlist " + aPlaylist.getName() + " to the queue.", false);
                    channel.sendMessage(embed.build()).queue();
                }
                else {
                    guildAudioManager.queue(firstTrack);
                    sendSongAddedMsg(firstTrack, channel);
                }
            }

            public void noMatches()
            {
                channel.sendMessage(uri + " not found.").queue();
            }

            public void loadFailed(FriendlyException exception) { }
        });
    }

    private static void sendSongAddedMsg(AudioTrack track, MessageChannel channel) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.ORANGE);
        embed.addField("Song added", "[" + track.getInfo().title + "](" +
                track.getInfo().uri +") has been added to the queue.", false);
        channel.sendMessage(embed.build()).queue();
    }
}
