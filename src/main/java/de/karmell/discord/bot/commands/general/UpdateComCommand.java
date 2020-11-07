package de.karmell.discord.bot.commands.general;

import de.karmell.discord.bot.Bot;
import de.karmell.discord.bot.commands.Command;
import de.karmell.discord.bot.commands.SimpleCommand;
import de.karmell.discord.bot.util.MessageUtil;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Iterator;
import java.util.List;

public class UpdateComCommand extends Command {
    public UpdateComCommand() {
        super(new String[]{"updatecom", "uc"}, CommandCategory.GENERAL, "Updates the result of given command.");
    }

    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(event.getChannelType().isGuild() && args.length > 1) {
            String response = args[1];
            for(int i = 2; i < args.length; i++) {
                response += " " + args[i];
            }
            List<SimpleCommand> scs = Bot.getJoinedGuilds().get(event.getGuild().getId()).getSimpleCommands();
            Iterator<SimpleCommand> itr = scs.iterator();
            while(itr.hasNext()) {
                SimpleCommand sc = itr.next();
                if(sc.getInvoke().equals(args[0])) sc.setResponse(response);
            }
            Bot.getDb().updateSimpleCommand(event.getGuild().getIdLong(), args[0], response);
            event.getChannel().sendMessage(MessageUtil.simpleMessage("Updated command **" + args[0] + "**.")).queue();
        }
    }
}
