package de.karmell.discord.bot.commands.general;

import de.karmell.discord.bot.Bot;
import de.karmell.discord.bot.commands.Command;
import de.karmell.discord.bot.commands.SimpleCommand;
import de.karmell.discord.bot.util.GuildWrapper;
import de.karmell.discord.bot.util.MessageUtil;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class AddComCommand extends Command {
    public AddComCommand() {
        super(new String[]{"addcom", "ac"}, CommandCategory.GENERAL, "Adds a new command with given name and result.");
    }

    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(event.getChannelType().isGuild()) {
            if(args.length > 1) {
                GuildWrapper wrapper = Bot.getJoinedGuilds().get(event.getGuild().getId());
                if(Bot.getCommandManager().getCommand(args[0]) == null && !wrapper.invokeInUse(args[0])) {
                    String result = args[1];
                    for(int i = 2; i < args.length; i++) {
                        result += " " + args[i];
                    }
                    SimpleCommand sc = new SimpleCommand(args[0], result);
                    wrapper.getSimpleCommands().add(sc);
                    Bot.getDb().addSimpleCommand(event.getGuild().getIdLong(), sc.getInvoke(), sc.getResponse());
                    event.getChannel().sendMessage(MessageUtil.simpleMessage("Command **" + args[0] + "** created.")).queue();
                } else {
                    event.getChannel().sendMessage(MessageUtil.errorMessage("The invoke you specified is already in use.")).queue();
                }
            }
        }
    }
}
