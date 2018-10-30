package de.karmell.discord.bot.commands;

import net.dv8tion.jda.core.entities.Role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages permissions for accessing commands (role based command access)
 */
public class CommandPermissionManager {
    private Map<String, List<Long>> invokeAccessibleRoles;

    public CommandPermissionManager() {
        invokeAccessibleRoles = new HashMap<>();
    }

    public void setAccessibleRoles(String invoke, Role... roles) {
        List<Long> roleIds = new ArrayList<>();
        for(Role r : roles) {
            roleIds.add(r.getIdLong());
        }
        invokeAccessibleRoles.put(invoke, roleIds);
    }

    public boolean canUse(String invoke, Role role) {
        List<Long> roles = invokeAccessibleRoles.get(invoke);
        if(roles != null && !roles.isEmpty()) {
            return roles.contains(role.getIdLong());
        }
        return false;
    }
}
