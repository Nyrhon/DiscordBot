package de.karmell.discord.bot.commands.general;

import de.karmell.discord.bot.Bot;
import de.karmell.discord.bot.commands.Command;
import de.karmell.discord.bot.util.DedicatedChannel;
import de.karmell.discord.bot.util.GuildWrapper;
import de.karmell.discord.bot.util.JDAHelper;
import de.karmell.discord.bot.util.MessageUtil;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigureCommand extends Command {
    public ConfigureCommand() {
        super(new String[]{"configure", "cfg"}, CommandCategory.GENERAL, "Shows information about guild specific configuration.");
    }

    @Override
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(event.getChannelType().isGuild()) {
            if(event.getMember().isOwner()) {
                if(args.length == 0) {
                    event.getTextChannel().sendMessage(MessageUtil.simpleMessage("Configuration",
                            "**!configure** p *cmd* *role* - restricts access to the given command to people who have the given role " +
                                    "(can be used multiple times to allow access for multiple roles)\n" +
                                    "**!configure** pr *cmd* *role* - removes access for given role to the command.\n" +
                                    "**!configure** c *channel* - adds given channel to dedicated channels (either name or id starting with id:).\n" +
                                    "**!configure** cr *channel* - removes given channel from dedicated channels (either name or id starting with id:).\n" +
                                    "**!configure** ca *channel* - enables / disables auto clear for the given channel (either name or id starting with id:).\n" +
                                    "**!configure** e - enables / disables exclusive mode (bot will only read / write in dedicated channels).\n" +
                                    "**!configure** d *cmd* - disables / re-enables the given command / category (*cmd* has to start with cc: for a category).")).queue();
                } else {
                    GuildWrapper wrapper = Bot.getJoinedGuilds().get(event.getGuild().getId());
                    switch(args[0]) {
                        case "p": {
                            if(args.length > 2) {
                                Command command = Bot.getCommandManager().getCommand(args[1]);
                                if(command == null) {
                                    event.getTextChannel().sendMessage(MessageUtil.errorMessage("Unkown command " + args[1])).queue();
                                } else {
                                    List<Role> roles = event.getGuild().getRoles();
                                    for(Role r : roles) {
                                        if(r.getName().toLowerCase().equals(args[2].toLowerCase())) {
                                            wrapper.addAccessibleRole(command.getAliases()[0], r);
                                            Bot.getDb().addAccessibleRole(event.getGuild().getIdLong(), command.getAliases()[0], r.getIdLong());
                                            event.getTextChannel().sendMessage(MessageUtil.simpleMessage("**"
                                                    + r.getName() + "** added to accessible roles for command **"
                                                    + args[1] + "**.")).queue();
                                            return;
                                        }
                                    }
                                }
                            }
                        } break;
                        case "pr": if(args.length > 2) {
                            Command command = Bot.getCommandManager().getCommand(args[1]);
                            if(command == null) {
                                event.getTextChannel().sendMessage(MessageUtil
                                        .errorMessage("Unknown command **" + args[1] + "**")).queue();
                            } else {
                                List<Role> roles = event.getGuild().getRoles();
                                for(Role r : roles) {
                                    if(r.getName().toLowerCase().equals(args[2].toLowerCase())) {
                                        wrapper.removeAccessibleRole(command.getAliases()[0], r);
                                        Bot.getDb().removeAccessibleRole(event.getGuild().getIdLong(), command.getAliases()[0], r.getIdLong());
                                        event.getTextChannel().sendMessage(
                                                MessageUtil.simpleMessage("**" + r.getName()
                                                        + "** removed from accessible roles for command **"
                                                        + args[1] + "**.")).queue();
                                        return;
                                    }
                                }
                            }
                        } break;
                        case "c": {
                            if(args.length > 1) {
                                TextChannel tx = JDAHelper.getTextChannelByNameOrId(args[1], event);
                                if(tx != null && !wrapper.containsChannel(tx.getIdLong())) {
                                    DedicatedChannel dc = new DedicatedChannel(tx.getIdLong());
                                    wrapper.getDedicatedChannels()
                                            .add(dc);
                                    Bot.getDb().addBotChannel(event.getGuild().getIdLong(), dc);
                                    event.getTextChannel().sendMessage(MessageUtil
                                            .simpleMessage("Added channel **" + tx.getName() + "** to dedicated channels.")).queue();
                                }
                            }
                        } break;
                        case "cr": {
                            if(args.length > 1) {
                                TextChannel tx = JDAHelper.getTextChannelByNameOrId(args[1], event);
                                if(tx != null && wrapper.containsChannel(tx.getIdLong())) {
                                    wrapper.removeChannel(tx.getIdLong());
                                    Bot.getDb().removeBotChannel(event.getGuild().getIdLong(), tx.getIdLong());
                                    event.getTextChannel().sendMessage(MessageUtil
                                            .simpleMessage("Removed channel **" + tx.getName() + "** from dedicated channels.")).queue();
                                }
                            }
                        } break;
                        case "ca": {
                            if(args.length > 1) {
                                TextChannel tx = JDAHelper.getTextChannelByNameOrId(args[1], event);
                                if(tx != null) {
                                    DedicatedChannel dc = wrapper.getDedicatedChannel(tx.getIdLong());
                                    if(dc != null) {
                                        dc.setAutoClear(!dc.isAutoClear());
                                        Bot.getDb().updateBotChannel(event.getGuild().getIdLong(), tx.getIdLong(), dc.isAutoClear());
                                        event.getChannel().sendMessage(MessageUtil.simpleMessage("**" + tx.getName() + "** "
                                        + (dc.isAutoClear() ? "is now being auto cleared." : "is not being auto cleared anymore."))).queue();
                                    }
                                }
                            }
                        } break;
                        case "e": {
                            wrapper.setExclusiveMode(!wrapper.isExclusiveMode());
                            Bot.getDb().saveGuildSettings(event.getGuild().getIdLong(), wrapper.isExclusiveMode());
                            event.getTextChannel().sendMessage(MessageUtil.simpleMessage(wrapper.isExclusiveMode()
                                    ? "The bot is now in exclusive mode." : "The bot is no longer in exclusive mode.")).queue();
                        } break;
                        case "d": {
                            if(args.length > 1) {
                                if(args[1].startsWith("cc:")) {
                                    String display = args[1].substring(args[1].indexOf(":") + 1);
                                    for(CommandCategory cc : CommandCategory.class.getEnumConstants()) {
                                        if(cc.getDisplay().toLowerCase().equals(display.toLowerCase())) {
                                            Bot.getCommandManager().getCommands().values().stream().distinct()
                                                    .sorted(Comparator.comparing(com -> com.getAliases()[0]))
                                                    .filter(c -> c.getCategory() == cc).collect(Collectors.toList())
                                                    .forEach(c -> switchDisableCommand(c, event, c.getAliases()[0], wrapper));
                                        }
                                    }
                                }
                                Command command = Bot.getCommandManager().getCommand(args[1]);
                                if(command != null) {
                                    if(command.getCategory() == CommandCategory.GENERAL) {
                                        event.getChannel().sendMessage(MessageUtil.errorMessage("You can't disable general commands.")).queue();
                                    } else {
                                        switchDisableCommand(command, event, args[1], wrapper);
                                    }
                                }
                            }
                        } break;
                        default: event.getTextChannel().sendMessage(MessageUtil.errorMessage("Unknown config param.")).queue();
                    }
                }
            }
        }
    }

    private void switchDisableCommand(Command command, MessageReceivedEvent event, String search, GuildWrapper wrapper) {
        List<String> disabledCommands = wrapper.getDisabledCommands();
        if(!disabledCommands.remove(command.getAliases()[0])) {
            disabledCommands.add(command.getAliases()[0]);
            Bot.getDb().addDisabledCommand(event.getGuild().getIdLong(), command.getAliases()[0]);
            event.getChannel().sendMessage(MessageUtil.simpleMessage("**" + search + "** is now disabled.")).queue();
        } else {
            Bot.getDb().removeDisabledCommand(event.getGuild().getIdLong(), command.getAliases()[0]);
            event.getChannel().sendMessage(MessageUtil.simpleMessage("**" + search + "** is now re-enabled.")).queue();
        }
    }
}
