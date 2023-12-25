package commands;

import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import model.ServerSettings;

public class StoreGameChannelCommand extends ListenerAdapter {
  private final ServerSettings ss;

  public StoreGameChannelCommand(ServerSettings ss) {
    this.ss = ss;
  }

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
    // If the event's command name is "set channel"
    if (event.getName().equals("set-channel")) {

      GuildChannelUnion channel = event.getOption("channel").getAsChannel();
      // If the channel is not a text channel
      if (!channel.getType().equals(ChannelType.TEXT)) {
        event.reply("Must set the channel to a Text Channel.").queue();
      } else {
        MessageChannel dedicatedChannel = channel.asTextChannel();
        this.ss.setDedicatedChannel(event.getGuild(), dedicatedChannel);
        this.ss.getDedicatedChannel(event).sendMessage("This channel will now be used for game statistics.").queue();

        event.reply("Channel set.").queue();
      }
    }
  }
}
