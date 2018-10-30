package de.karmell.discord.bot.commands.general;

import de.karmell.discord.bot.Bot;
import de.karmell.discord.bot.commands.Command;
import de.karmell.discord.bot.util.MessageUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Help command to get a list of all commands / description of a single command.
 */
public class HelpCommand extends Command {
    public HelpCommand() {
        super(new String[]{"help", "h"}, CommandCategory.GENERAL, "Shows information about a specific / all command(s)");
    }

    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(args.length > 0) {
            Command command = Bot.getCommandManager().getCommand(args[0]);
            if(command == null) {
                event.getChannel().sendMessage(MessageUtil.errorMessage("Unknown command.")).queue();
            } else {
                event.getChannel().sendMessage(MessageUtil.simpleMessage("Description for: " + args[0],
                        command.getDescription())).queue();
            }
        } else {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(MessageUtil.INFO_COLOR);
            embedBuilder.addField("Guild settings", "`prefix`: " + Bot.getConfig().COMMAND_PREFIX + "\n", false);
            for(CommandCategory cc : CommandCategory.class.getEnumConstants()) {
                StringBuilder cd = new StringBuilder();
                // sorts and filters all commands for the currently iterated category
                Bot.getCommandManager().getCommands().values().stream().distinct()
                        .sorted(Comparator.comparing(com -> com.getAliases()[0]))
                        .filter(c -> c.getCategory() == cc).collect(Collectors.toList())
                        .forEach(c -> cd.append("`" + c.getAliases()[0] + ":` " + c.getDescription()+ "\n"));
                String cds = cd.toString();
                if(cds.contains("\n")) {
                    cds = cds.substring(0, cds.length() - 2);
                }
                if(!cds.isEmpty()) {
                    embedBuilder.addField(cc.getDisplay(), cds, false);
                }
            }
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        }
    }
}
