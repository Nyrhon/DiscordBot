package de.karmell.discord.bot.listeners;

import de.karmell.discord.bot.Bot;
import de.karmell.discord.bot.util.GuildWrapper;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public class GuildJoinedListener {
    @SubscribeEvent
    private void onGuildJoin(GuildJoinEvent event) {
        Guild guild = event.getGuild();
        Bot.getJoinedGuilds().put(guild.getId(), new GuildWrapper(guild.getId()));
    }
}
