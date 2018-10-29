package de.karmell.discord.bot.listeners;

import de.karmell.discord.bot.Bot;
import de.karmell.discord.bot.core.GuildWrapper;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

public class ReadyListener {
    @SubscribeEvent
    private void onReady(ReadyEvent event) {
        event.getJDA().getGuilds().forEach(guild ->
            Bot.getJoinedGuilds().put(guild.getId(), new GuildWrapper(guild.getId()))
        );
    }
}
