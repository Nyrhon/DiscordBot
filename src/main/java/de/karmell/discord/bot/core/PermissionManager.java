package de.karmell.discord.bot.core;

import net.dv8tion.jda.core.entities.Role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionManager {
    private Map<String, List<String>> invokeAccessibleRoles;

    public PermissionManager() {
        invokeAccessibleRoles = new HashMap<>();
    }

    public void setAccessibleRoles(String invoke, Role... roles) {
        List<String> roleIds = new ArrayList<>();
        for(Role r : roles) {
            roleIds.add(r.getId());
        }
        invokeAccessibleRoles.put(invoke, roleIds);
    }

    public boolean canUse(String invoke, Role role) {
        List<String> roles = invokeAccessibleRoles.get(invoke);
        if(roles != null && !roles.isEmpty()) {
            return roles.contains(role.getId());
        }
        return false;
    }
}
