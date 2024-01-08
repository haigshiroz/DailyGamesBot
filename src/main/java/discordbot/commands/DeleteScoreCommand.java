package discordbot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Date;
import java.util.GregorianCalendar;

import discordbot.data.GameType;
import discordbot.data.FirebaseService;
import discordbot.data.ScoreData;
import discordbot.visitor.visitors.ValidateScore;

public class DeleteScoreCommand extends ListenerAdapter {
  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
    // If the event's command name is "delete-score"
    if (event.getName().equals("delete-score")) {
      // Defer reply to give time to contact database.
      event.deferReply().setEphemeral(true).queue();
      String reply = "Error removing data.";

      OptionMapping gameOption = event.getOption("game");
      OptionMapping dateOption = event.getOption("date");

      // If the required options are not null, validate them and convert them to Strings.
      if(gameOption != null && dateOption != null) {
        String gameString = gameOption.getAsString();
        String dateString = dateOption.getAsString();

        if(GameType.stringIsGameType(gameString) && this.isValidDate(dateString)) {
          GameType game = GameType.stringToGameType(gameString);
          String[] dateSplit = dateString.split("/");
          int month = Integer.parseInt(dateSplit[0]);
          int day = Integer.parseInt(dateSplit[1]);
          int year = Integer.parseInt(dateSplit[2]);
          Date date = new GregorianCalendar(year, month - 1, day).getTime();

          // Score does not matter.
          ScoreData data = new ScoreData(event.getUser(), game, date, "", event.getGuild());
          // Remove data from database.
          boolean dataFound = FirebaseService.deleteScore(data);
          if(dataFound) {
            reply = "Data successfully removed.";
          } else {
            reply = "No data found.";
          }
        }
      }
      // Reply to interaction (command).
      event.getHook().editOriginal(reply).queue();
    }
  }

  private boolean isValidDate(String date) {
    if (date == null) {
      throw new IllegalArgumentException("Given date string was null.");
    }
    boolean ret = false;
    String[] dateSplit = date.split("/");
    if (dateSplit.length == 3) {
      ret = ValidateScore.isStringInteger(dateSplit[0]) && ValidateScore.isStringInteger(dateSplit[1]) && ValidateScore.isStringInteger(dateSplit[2]);
    }

    return ret;
  }
}
