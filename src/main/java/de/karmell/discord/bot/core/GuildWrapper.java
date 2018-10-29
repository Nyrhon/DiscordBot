package de.karmell.discord.bot.core;

import java.util.ArrayList;
import java.util.List;

public class GuildWrapper {
    private String guildId;
    private List<SimpleCommand> simpleCommands;
    private List<String> disabledCommands;
    private String[] dedicatedChannels;
    private boolean[] autoClear;
    private boolean exclusive;

    public GuildWrapper(String guildId) {
        this.guildId = guildId;
        simpleCommands = new ArrayList<>(); // TODO build up from database
        disabledCommands = new ArrayList<>(); // TODO build up from database
        dedicatedChannels = new String[0];
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
}
