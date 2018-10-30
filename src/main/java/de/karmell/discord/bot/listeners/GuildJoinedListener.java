package de.karmell.discord.bot.listeners;

import de.karmell.discord.bot.Bot;
import de.karmell.discord.bot.util.GuildWrapper;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

public class GuildJoinedListener {
    @SubscribeEvent
    private void onGuildJoin(GuildJoinEvent event) {
        Bot.getJoinedGuilds().put(event.getGuild().getId(), new GuildWrapper(event.getGuild().getId()));
    }
}
