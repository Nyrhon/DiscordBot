package de.karmell.discord.bot.commands;

import de.karmell.discord.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class StopCommand implements Command {
    public void invoke(String[] args, MessageReceivedEvent event) {
        if(!event.getAuthor().getId().equals(Main.CONFIG.BOT_OWNER)) {
            event.getChannel().sendMessage("You're not my dad! :baby:").queue();
        } else {
            /*if(event.getChannel().getType().isGuild()) {
                GuildAudioManager manager = Main.GUILD_MUSIC_MANAGERS.get(event.getGuild());
                if(manager == null) {
                    manager = new GuildAudioManager(Main.AUDIO_PLAYER_MANAGER);
                    manager.getPlayer().setVolume(100);
                }
                List<VoiceChannel> channels = event.getGuild().getVoiceChannels();
                VoiceChannel channelToJoin = null;
                for(VoiceChannel channel : channels) {
                    List<Member> members = channel.getMembers();
                    for(Member member : members) {
                        if(member.getUser().getId().equals(event.getAuthor().getId())) {
                            channelToJoin = channel;
                        }
                    }
                }
                if(channelToJoin != null) {
                    PlayCommand.addToQueue(manager, event.getChannel(), "https://www.youtube.com/watch?v=-YEG9DgRHhA", false);
                    event.getGuild().getAudioManager().setSendingHandler(manager.getSendHandler());
                    event.getGuild().getAudioManager().openAudioConnection(channelToJoin);
                } else {
                    event.getChannel().sendMessage("https://www.youtube.com/watch?v=-YEG9DgRHhA").complete();
                }
            } else {
                event.getChannel().sendMessage("https://www.youtube.com/watch?v=-YEG9DgRHhA").complete();
            }
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    event.getJDA().shutdownNow();
                    System.exit(0);
                }
            }, 5000);*/
            event.getJDA().shutdownNow();
            System.exit(0);
        }
    }

    public String describe() {
        return "Stops the bot";
    }
}
