package de.karmell.discord.bot.commands.general;

import de.karmell.discord.bot.Bot;
import de.karmell.discord.bot.commands.Command;
import de.karmell.discord.bot.commands.SimpleCommand;
import de.karmell.discord.bot.util.GuildWrapper;
import de.karmell.discord.bot.util.MessageUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Help command to get a list of all commands / description of a single command.
 */
public class HelpCommand extends Command {
    public HelpCommand() {
        super(new String[]{"help", "h"}, CommandCategory.GENERAL, "Shows information about a specific / all command(s) or a category.");
    }

    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(args.length > 0) {
            String cs = args[0];
            for(int i = 1; i < args.length; i++) {
                cs += " " + args[i];
            }
            Command command = Bot.getCommandManager().getCommand(cs);
            if(command == null) {
                for(CommandCategory cc : CommandCategory.class.getEnumConstants()) {
                    if(cc.getDisplay().toLowerCase().equals(cs.toLowerCase())) {
                        String cd = parseCommandCategory(cc, event);
                        if(cd.contains("\n")) {
                            cd = cd.substring(0, cd.length() - 2);
                        }
                        if(!cd.isEmpty()) {
                            event.getChannel().sendMessage(MessageUtil.simpleMessage(cc.getDisplay(), cd)).queue();
                        }
                        return;
                    }
                }
                event.getChannel().sendMessage(MessageUtil.errorMessage("Unknown command.")).queue();
            } else {
                event.getChannel().sendMessage(MessageUtil.simpleMessage("Description for: " + args[0],
                        command.getDescription())).queue();
            }
        } else {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(MessageUtil.INFO_COLOR);
            for(CommandCategory cc : CommandCategory.class.getEnumConstants()) {
                String cd = parseCommandCategory(cc, event);
                if(cd.contains("\n")) {
                    cd = cd.substring(0, cd.length() - 2);
                }
                if(!cd.isEmpty()) {
                    embedBuilder.addField(cc.getDisplay(), cd, false);
                }
            }
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        }
    }

    private String parseCommandCategory(CommandCategory cc, MessageReceivedEvent event) {
        StringBuilder cd = new StringBuilder();
        if(cc.equals(CommandCategory.GUILD_SPECIFIC)) {
            List<SimpleCommand> scs = Bot.getJoinedGuilds().get(event.getGuild().getId()).getSimpleCommands();
            for(int i = 0; i < scs.size(); i++) {
                cd.append("`" + scs.get(i).getInvoke() + "`" + (i == scs.size() - 1 ? "" : ","));
            }
        } else {
            // sorts and filters all commands for the currently iterated category
            Bot.getCommandManager().getCommands().values().stream().distinct()
                    .sorted(Comparator.comparing(com -> com.getAliases()[0]))
                    .filter(c -> c.getCategory() == cc).collect(Collectors.toList())
                    .forEach(c -> {
                        if(!c.getAliases()[0].equals("stop")) {
                            if (event.getChannelType().isGuild()) {
                                GuildWrapper wrapper = Bot.getJoinedGuilds().get(event.getGuild().getId());
                                if (!wrapper.getDisabledCommands().contains(c.getAliases()[0]) && wrapper.canAccess(c.getAliases()[0], event.getMember().getRoles())) {
                                    cd.append("`" + c.getAliases()[0] + ":` " + c.getDescription() + "\n");
                                }
                            } else {
                                cd.append("`" + c.getAliases()[0] + ":` " + c.getDescription() + "\n");
                            }
                        }
                    });
        }
        return cd.toString();
    }
}
