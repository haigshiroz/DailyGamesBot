package commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import model.ServerSettings;
import model.game.GameType;
import model.game.data.FirebaseService;

public class CompareCommand extends ListenerAdapter {
  private final ServerSettings ss;

  public CompareCommand(ServerSettings ss) {
    this.ss = ss;
  }

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
    event.deferReply().queue();
    // If the event's command name is "compare"
    if (event.getName().equals("compare")) {
      OptionMapping gameOption = event.getOption("game");
      OptionMapping dateOption = event.getOption("date");

      String gameString = null;
      String dateString = null;
      GameType game = null;
      Date date = null;

      if(gameOption != null) {
        gameString = gameOption.getAsString();
      }
      if(dateOption != null) {
        dateString = dateOption.getAsString();
      }

      if(gameString != null && GameType.stringIsGameType(gameString)) {
        game = GameType.stringToGameType(gameString);
      }
      if(dateString != null && this.isValidDate(dateString)) {
        String[] dateSplit = dateString.split("/");
        int month = Integer.parseInt(dateSplit[0]);
        int day = Integer.parseInt(dateSplit[1]);
        int year = Integer.parseInt(dateSplit[2]);
        date = new GregorianCalendar(year, month-1, day).getTime();
      }

      if(game != null && date != null) {
        event.getHook().editOriginal(new FirebaseService().getScoreForGameForDay(game, date)).queue();
      } else if (game != null) {

      } else if(date != null) {

      } else {
        // Default
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        date = today.getTime();
        System.out.println(date);

        String reply = "";
        for(GameType gt : GameType.values()) {
          reply += new FirebaseService().getScoreForGameForDay(gt, date);
        }
        if(reply.isEmpty()) {
          event.getHook().editOriginal("No scores for today.").queue();
        } else {
          event.getHook().editOriginal(reply).queue();
        }
      }

    }


  }

  private boolean isValidDate(String date) {
    if(date == null) {
      throw new IllegalArgumentException("Given date string was null.");
    }
    boolean ret = false;
    String[] dateSplit = date.split("/");
    if(dateSplit.length == 3) {
      ret = this.isStringInteger(dateSplit[0]) && this.isStringInteger(dateSplit[1]) && this.isStringInteger(dateSplit[2]);
    }

    return ret;
  }

  private boolean isStringInteger(String str) {
    // Check if it's null and if the length is greater than 0
    if (str == null) {
      return false;
    }
    int length = str.length();
    if (length == 0) {
      return false;
    }

    for (int ind = 0; ind < length; ind++) {
      char c = str.charAt(ind);
      if (c < '0' || c > '9') {
        // If the char is not 0-9, return false
        return false;
      }
    }

    // Otherwise, all chars are between 0-9 so it's an integer.
    return true;
  }
}
