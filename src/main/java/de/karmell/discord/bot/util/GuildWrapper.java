package de.karmell.discord.bot.util;

import de.karmell.discord.bot.commands.SimpleCommand;
import net.dv8tion.jda.core.entities.Role;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Wrapper to contain additional information about a guild to store in a database.
 */
public class GuildWrapper {
    private String guildId;
    private List<SimpleCommand> simpleCommands;
    private List<String> disabledCommands;
    private List<DedicatedChannel> dedicatedChannels;
    private boolean exclusiveMode;
    private Map<String, List<Long>> accessibleRoles;

    public GuildWrapper(String guildId) {
        this.guildId = guildId;
        simpleCommands = new ArrayList<>(); // TODO build up from database
        disabledCommands = new ArrayList<>(); // TODO build up from database
        dedicatedChannels = new ArrayList<>();
        exclusiveMode = false;
        accessibleRoles = new HashMap<>();
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

    public List<DedicatedChannel> getDedicatedChannels() {
        return dedicatedChannels;
    }

    public void setDedicatedChannels(List<DedicatedChannel> dedicatedChannels) {
        this.dedicatedChannels = dedicatedChannels;
    }

    public boolean isAutoClear(long channelID) {
        for(DedicatedChannel dc : dedicatedChannels) {
            if(dc.getChannelId() == channelID) return dc.isAutoClear();
        }
        return false;
    }

    public boolean containsChannel(long channelID) {
        for(DedicatedChannel dc : dedicatedChannels) {
            if(dc.getChannelId() == channelID) return true;
        }
        return false;
    }

    public DedicatedChannel getDedicatedChannel(long channelID) {
        for(DedicatedChannel dc : dedicatedChannels) {
            if(dc.getChannelId() == channelID) return dc;
        }
        return null;
    }

    public void removeChannel(long channelID) {
        Iterator<DedicatedChannel> itr = dedicatedChannels.iterator();
        while(itr.hasNext()) {
            if(itr.next().getChannelId() == channelID) itr.remove();

        }
    }

    public boolean isExclusiveMode() {
        return exclusiveMode;
    }

    public void setExclusiveMode(boolean exclusiveMode) {
        this.exclusiveMode = exclusiveMode;
    }

    public boolean canAccess(String invoke, List<Role> roles) {
        for(Role role : roles) {
            if(canAccess(invoke, role.getIdLong())) {
                return true;
            }
        }
        return false;
    }

    public boolean canAccess(String invoke, long roleID) {
        List<Long> roleIDs = accessibleRoles.get(invoke);
        return roleIDs == null || roleIDs.isEmpty() || !roleIDs.stream().filter(id -> id == roleID).collect(Collectors.toList()).isEmpty();
    }

    public void setAccessibleRoles(Map<String, List<Long>> accessibleRoles) {
        this.accessibleRoles = accessibleRoles;
    }

    public void addAccessibleRole(String invoke, Role role) {
        if(!accessibleRoles.containsKey(invoke)) {
            accessibleRoles.put(invoke, new ArrayList<>());
        }
        accessibleRoles.get(invoke).add(role.getIdLong());
    }

    public void removeAccessibleRole(String invoke, Role role) {
        if(accessibleRoles.containsKey(invoke)) {
            accessibleRoles.get(invoke).remove(role.getIdLong());
        }
    }

    public Map<String, List<Long>> getAccessibleRoles() { return accessibleRoles; }

    public boolean invokeInUse(String invoke) {
        for(SimpleCommand sc : simpleCommands) {
            if(sc.getInvoke().equals(invoke)) {
                return  true;
            }
        }
        return false;
    }
}
