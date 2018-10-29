package de.karmell.discord.bot.util;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class MessageUtil {
    public static final Color ERROR_COLOR = Color.RED;
    public static final Color INFO_COLOR = Color.ORANGE;

    public static MessageEmbed simpleMessage(String message) {
        return newEmbed("", message, true, INFO_COLOR);
    }

    public static MessageEmbed simpleMessage(String header, String message) {
        return newEmbed(header, message, false, INFO_COLOR);
    }

    public static MessageEmbed errorMessage(String message) {
        return newEmbed("", message, true, ERROR_COLOR);
    }

    public static MessageEmbed errorMessage(String header, String message) {
        return newEmbed(header, message, false, ERROR_COLOR);
    }

    public static MessageEmbed songInfo(String header, AudioTrackInfo trackInfo) {
        Duration d = Duration.of(trackInfo.length, ChronoUnit.MILLIS);
        return newEmbed(header, "[" + trackInfo.title + "](" + trackInfo.uri + ")\n`Duration:` " +
                String.format("%d:%02d%n", d.toMinutes(), d.minusMinutes(d.toMinutes()).getSeconds()), false, INFO_COLOR);
    }

    private static MessageEmbed newEmbed(String header, String message, boolean inline, Color color) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(color);
        if(!header.isEmpty()) {
            embedBuilder.addField(header, message, inline);
        } else {
            embedBuilder.setDescription(message);
        }
        return embedBuilder.build();
    }
}