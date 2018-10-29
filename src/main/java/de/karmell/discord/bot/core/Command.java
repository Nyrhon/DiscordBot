package de.karmell.discord.bot.core;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract class Command {
    protected String[] aliases;
    protected CommandCategory category;
    protected String description;

    protected Command(String[] aliases, CommandCategory category, String description) {
        this.aliases = aliases;
        this.category = category;
        this.description = description;
    }

    public abstract void invoke(String[] args, MessageReceivedEvent event);

    public String[] getAliases() {
        return aliases;
    }

    public void setAliases(String[] aliases) {
        this.aliases = aliases;
    }

    public CommandCategory getCategory() {
        return category;
    }

    public void setCategory(CommandCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public enum CommandCategory {
        GENERAL("General"),
        MUSIC("Music"),
        GUILD_SPECIFIC("Guild Specific");

        private String display;

        CommandCategory(String display) { this.display = display; }

        public String getDisplay() { return display; }
    }
}
