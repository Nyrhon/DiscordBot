package de.karmell.discord.bot.commands.general;

import de.karmell.discord.bot.Bot;
import de.karmell.discord.bot.commands.Command;
import de.karmell.discord.bot.util.DedicatedChannel;
import de.karmell.discord.bot.util.GuildWrapper;
import de.karmell.discord.bot.util.MessageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class InfoCommand extends Command {
    public InfoCommand() {
        super(new String[]{"info"}, CommandCategory.GENERAL, "Shows information about the current guild configuration.");
    }

    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(event.getChannelType().isGuild()) {
            GuildWrapper wrapper = Bot.getJoinedGuilds().get(event.getGuild().getId());
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(MessageUtil.INFO_COLOR);
            StringBuilder sb = new StringBuilder();
            for(String invoke : wrapper.getDisabledCommands()) {
                sb.append("**" + invoke + "**, ");
            }
            String s = sb.toString();
            if(s.contains(", "))
                s = s.substring(0, s.length() - 2);
            if(!s.isEmpty())
                embedBuilder.addField("Disabled Commands", s , false);
            sb.delete(0, sb.length());
            for(DedicatedChannel channel : wrapper.getDedicatedChannels()) {
                sb.append("*" +  event.getGuild().getTextChannelById(channel.getChannelId()).getName() + "*, ");
            }
            s = sb.toString();
            if(s.contains(", "))
                s = s.substring(0, s.length() - 2);
            String exclusiveMode = "`Exclusive mode:` " + (wrapper.isExclusiveMode() ? "yes" : "no");
            embedBuilder.addField("Dedicated Channels", s.isEmpty() ? exclusiveMode : exclusiveMode + "\n`Channels:` " + s , false);
            sb.delete(0, sb.length());
            wrapper.getAccessibleRoles().forEach((invoke, roleIDs) -> {
                sb.append("`" + invoke + ":` ");
                for(int i = 0; i < roleIDs.size(); i++) {
                    Role r = event.getGuild().getRoleById(roleIDs.get(i));
                    sb.append(r.getName() + (i == roleIDs.size() - 1 ? "\n" : ", "));
                }
            });
            s = sb.toString();
            if(!s.isEmpty())
                embedBuilder.addField("Permission Control", "Following commands restrict access to shown roles.\n" + s, false);
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        }
    }
}
