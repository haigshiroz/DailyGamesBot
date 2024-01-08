package discordbot.commands;

import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.ExecutionException;

import discordbot.data.FirebaseService;

public class StoreGameChannelCommand extends ListenerAdapter {
  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
    // If the event's command name is "set channel"
    if (event.getName().equals("set-channel")) {
      // Defer reply since it takes a while to connect to database.
      event.deferReply().queue();

      GuildChannelUnion channel = event.getOption("channel").getAsChannel();
      // If the channel is not a text channel
      if (!channel.getType().equals(ChannelType.TEXT)) {
        event.getHook().editOriginal("Must set the channel to a Text Channel.").queue();
      } else {
        // Set the dedicated channel.
        try {
          // Save the channel.
          FirebaseService.saveServerSettings(event.getGuild(), channel.asTextChannel());
          // Retrieve the saved channel and send a message confirming its dedication.
          MessageChannel dedicated = FirebaseService.getDedicatedChannel(event.getGuild(), event.getChannel().asTextChannel());
          dedicated.sendMessage("This channel will now be used for game statistics.").queue();
          // Reply to original command.
          event.getHook().editOriginal("Channel set.").queue();
        } catch (ExecutionException | InterruptedException e) {
          e.printStackTrace();
          event.getHook().editOriginal("Error setting text channel.").queue();
        }
      }
    }
  }
}
