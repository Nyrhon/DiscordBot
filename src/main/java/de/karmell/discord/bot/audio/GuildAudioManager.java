package de.karmell.discord.bot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import de.karmell.discord.bot.Bot;
import de.karmell.discord.bot.util.MessageUtil;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.*;
import java.util.List;

/**
 * Manages everything audio related for a single guild.
 */
public class GuildAudioManager extends AudioEventAdapter {
    private final AudioPlayer player;
    private final AudioPlayerSendHandler sendHandler;
    private final Queue<AudioTrack> queue;
    private boolean loop = false;
    private boolean shuffle = false;
    private Guild guild;
    private MessageChannel channel;

    public GuildAudioManager(AudioPlayerManager manager) {
        player = manager.createPlayer();
        sendHandler = new AudioPlayerSendHandler(player);
        player.addListener(this);
        queue = new LinkedList<>();
    }

    public AudioPlayer getPlayer() {
        return player;
    }
    public AudioPlayerSendHandler getSendHandler() {
        return sendHandler;
    }
    public void setGuild(Guild guild) { this.guild = guild; }
    public void setMessageChannel(MessageChannel channel) { this.channel = channel; }
    public MessageChannel getMessageChannel() { return channel; }
    public boolean isLoop()
    {
        return loop;
    }
    public void setLoop(boolean loop)
    {
        this.loop = loop;
    }
    public void setShuffle(boolean shuffle) { this.shuffle = shuffle; }
    public boolean isShuffling() { return shuffle; }
    public Queue<AudioTrack> getQueue() {
        return queue;
    }

    /**
     * Queues the specified AudioTrack
     * @param track to be queued
     */
    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    /**
     * Skips the current song and sets a Timer task to leave the voice channel when inactive.
     */
    public void skip() {
        if(queue.isEmpty()) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(queue.isEmpty() && player.getPlayingTrack() == null) {
                        guild.getAudioManager().setSendingHandler(null);
                        guild.getAudioManager().closeAudioConnection();
                        Bot.getGuildAudioManagers().remove(guild.getId());
                    }
                }
            }, 15000);
        }
        if(shuffle) {
            Collections.shuffle((List<AudioTrack>) queue);
        }
        loop = false;
        player.startTrack(queue.poll(), false);
        if(player.getPlayingTrack() != null) {
            channel.sendMessage(MessageUtil.songInfo("Now playing", player.getPlayingTrack().getInfo())).queue();
        }
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason)
    {
        if(endReason.mayStartNext) {
            if (loop) {
                player.startTrack(track.makeClone(), false);
                channel.sendMessage(MessageUtil.songInfo("Now playing", player.getPlayingTrack().getInfo())).queue();
            } else {
                skip();
            }
        }
    }
}
