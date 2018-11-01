package de.karmell.discord.bot.util;

public class DedicatedChannel {
    private long channelId;
    private boolean autoClear;

    public DedicatedChannel() {
        autoClear = false;
    }

    public DedicatedChannel(long channelId) {
        this.channelId = channelId;
        autoClear = false;
    }

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
}
