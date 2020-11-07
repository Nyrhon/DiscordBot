package de.karmell.discord.bot.commands.general;

import de.karmell.discord.bot.Bot;
import de.karmell.discord.bot.commands.Command;
import de.karmell.discord.bot.commands.SimpleCommand;
import de.karmell.discord.bot.util.MessageUtil;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Iterator;
import java.util.List;

public class RemoveComCommand extends Command {
    public RemoveComCommand() {
        super(new String[]{"removecom", "rmc"}, CommandCategory.GENERAL, "Removes the specified guild command.");
    }

    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(event.getChannelType().isGuild() && args.length > 0) {
            List<SimpleCommand> scs = Bot.getJoinedGuilds().get(event.getGuild().getId()).getSimpleCommands();
            Iterator<SimpleCommand> itr = scs.iterator();
            while(itr.hasNext()) {
                SimpleCommand sc = itr.next();
                if(sc.getInvoke().equals(args[0])) itr.remove();
            }
            Bot.getDb().removeSimpleCommand(event.getGuild().getIdLong(), args[0]);
            event.getChannel().sendMessage(MessageUtil.simpleMessage("Removed command **" + args[0] + "**.")).queue();
        }
    }
}
