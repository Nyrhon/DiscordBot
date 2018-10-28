package de.karmell.discord.bot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import de.karmell.discord.bot.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.awt.Color;
import java.util.*;
import java.util.List;

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

    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    public void skip() {
        if(queue.isEmpty()) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    guild.getAudioManager().setSendingHandler(null);
                    guild.getAudioManager().closeAudioConnection();
                    Main.GUILD_MUSIC_MANAGERS.remove(guild.getId());
                }
            }, 15000);
        }
        if(shuffle) {
            Collections.shuffle((List<AudioTrack>) queue);
        }
        loop = false;
        player.startTrack(queue.poll(), false);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.ORANGE);
        embed.addField("Now playing", "[" + player.getPlayingTrack().getInfo().title + "](" +
                player.getPlayingTrack().getInfo().uri +")", false);
        channel.sendMessage(embed.build()).queue();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason)
    {
        if (loop) {
            player.startTrack(track.makeClone(), false);
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.ORANGE);
            embed.addField("Now playing", "[" + player.getPlayingTrack().getInfo().title + "](" +
                    player.getPlayingTrack().getInfo().uri +")", false);
            channel.sendMessage(embed.build()).queue();
        } else {
            skip();
        }
    }
}
