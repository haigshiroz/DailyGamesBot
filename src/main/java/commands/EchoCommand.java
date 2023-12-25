package commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import model.ServerSettings;

/**
 * Event Listener for the Echo Command
 */
public class EchoCommand extends ListenerAdapter {
  private final ServerSettings ss;

  public EchoCommand(ServerSettings ss) {
    this.ss = ss;
  }

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
    // If the event's command name is echo
    if(event.getName().equals("echo")) {
      // Get the "message" option of the event
      String message = event.getOption("message").getAsString();

      this.ss.getDedicatedChannel(event).sendMessage(message).queue();
      event.reply("Echoed.").setEphemeral(true).queue();
    }
  }
}
