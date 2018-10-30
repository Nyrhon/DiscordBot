package de.karmell.discord.bot.util;

import de.karmell.discord.bot.commands.SimpleCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper to contain additional information about a guild to store in a database.
 */
public class GuildWrapper {
    private String guildId;
    private List<SimpleCommand> simpleCommands;
    private List<String> disabledCommands;
    private long[] dedicatedChannels;
    private boolean[] autoClear;
    private boolean exclusive;

    public GuildWrapper(String guildId) {
        this.guildId = guildId;
        simpleCommands = new ArrayList<>(); // TODO build up from database
        disabledCommands = new ArrayList<>(); // TODO build up from database
        dedicatedChannels = new long[0];
        autoClear = new boolean[0];
        exclusive = false;
    }

    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    public List<SimpleCommand> getSimpleCommands() {
        return simpleCommands;
    }

    public void setSimpleCommands(List<SimpleCommand> simpleCommands) {
        this.simpleCommands = simpleCommands;
    }

    public List<String> getDisabledCommands() {
        return disabledCommands;
    }

    public void setDisabledCommands(List<String> disabledCommands) {
        this.disabledCommands = disabledCommands;
    }

    public long[] getDedicatedChannels() {
        return dedicatedChannels;
    }

    public void setDedicatedChannels(long[] dedicatedChannels) {
        this.dedicatedChannels = dedicatedChannels;
    }

    public boolean[] getAutoClear() {
        return autoClear;
    }

    public void setAutoClear(boolean[] autoClear) {
        this.autoClear = autoClear;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }
}
