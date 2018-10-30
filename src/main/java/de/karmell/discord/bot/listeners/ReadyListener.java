package de.karmell.discord.bot.listeners;

import de.karmell.discord.bot.Bot;
import de.karmell.discord.bot.util.GuildWrapper;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

public class ReadyListener {
    @SubscribeEvent
    private void onReady(ReadyEvent event) {
        event.getJDA().getGuilds().forEach(guild -> {
            GuildWrapper gw = new GuildWrapper(guild.getId());
            Bot.getDb().initBotChannels(guild.getIdLong(), gw);
            Bot.getJoinedGuilds().put(guild.getId(), gw);
        });
    }
}
