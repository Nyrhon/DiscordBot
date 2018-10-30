package de.karmell.discord.bot.util;

public class DedicatedChannel {
    private long channelId;
    private boolean autoClear;
    private boolean exclusive;

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public boolean isAutoClear() {
        return autoClear;
    }

    public void setAutoClear(boolean autoClear) {
        this.autoClear = autoClear;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }
}
