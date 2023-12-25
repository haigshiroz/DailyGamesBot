package commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import model.ServerSettings;

public class WordleCommand extends ListenerAdapter {
  private final ServerSettings ss;

  public WordleCommand(ServerSettings ss) {
    this.ss = ss;
  }

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
    // If the event's command name is echo
    if(event.getName().equals("wordle")) {
      // Get the "score" option of the event. This is the Wordle Score.
      String message = event.getOption("score").getAsString();

      // Store the score into a database for each user
      System.out.println(message);

      // Reply privately as confirmation.
      event.reply("Score tracked.").setEphemeral(true).queue();
    }
  }
}