package discordbot.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import discordbot.game.GameType;
import discordbot.game.data.FirebaseService;
import discordbot.game.visitor.visitors.ValidateScore;

public class CompareCommand extends ListenerAdapter {
  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
    // If the event's command name is "compare"
    if (event.getName().equals("compare")) {
      // Defer reply due to long wait time of accessing database.
      event.deferReply().setEphemeral(true).queue();

      // Game and Date are optional fields and thus may not be provided.
      // Check if they are present and execute a query accordingly.
      OptionMapping gameOption = event.getOption("game");
      OptionMapping dateOption = event.getOption("date");
      String gameString = null;
      String dateString = null;
      GameType game = null;
      Date date = null;

      if (gameOption != null) {
        // If the game option is present, get the provided String.
        gameString = gameOption.getAsString();
      }
      if (dateOption != null) {
        // If the date option is present, get the provided String.
        dateString = dateOption.getAsString();
      }

      if (gameString != null && GameType.stringIsGameType(gameString)) {
        // If the game's string is valid, obtain the GameType object.
        game = GameType.stringToGameType(gameString);
      }
      if (dateString != null && this.isValidDate(dateString)) {
        // If the date's string is valid, obtain the Date object.
        String[] dateSplit = dateString.split("/");
        int month = Integer.parseInt(dateSplit[0]);
        int day = Integer.parseInt(dateSplit[1]);
        int year = Integer.parseInt(dateSplit[2]);
        date = new GregorianCalendar(year, month - 1, day).getTime();
      }

      String reply = "";
      Guild server = event.getGuild();
      if (server == null) {
        reply = "Error finding server ID";
      } else if (game != null && date != null) {
        // If both game and date were provided, query them.
        reply = FirebaseService.getScoreForGameForDay(game, date, server);
      } else if (game != null) {
        // If only game was provided, query it and today's date.
        date = this.todaysDate();
        reply = FirebaseService.getScoreForGameForDay(game, date, server);
      } else if (date != null) {
        // If only date was provided, query it and every game.
        for (GameType gt : GameType.values()) {
          reply += FirebaseService.getScoreForGameForDay(gt, date, server);
        }
      } else {
        // Default, query today's date and every game.
        date = this.todaysDate();

        for (GameType gt : GameType.values()) {
          reply += FirebaseService.getScoreForGameForDay(gt, date, server);
        }
      }

      if (reply.isEmpty()) {
        event.getHook().editOriginal("No scores yet for that day.").queue();
      } else {
        event.getHook().editOriginal(reply).queue();
      }
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

  /**
   * Gets today's date as a Date object with a time of 0.
   *
   * @return Today's date.
   */
  private Date todaysDate() {
    Calendar today = Calendar.getInstance();
    today.set(Calendar.HOUR_OF_DAY, 0);
    today.set(Calendar.MINUTE, 0);
    today.set(Calendar.SECOND, 0);
    today.set(Calendar.MILLISECOND, 0);
    return today.getTime();
  }
}
