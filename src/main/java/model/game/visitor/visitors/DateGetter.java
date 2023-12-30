package model.game.visitor.visitors;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A class that implements the IGameVisitor that returns the date of the game that was
 * submitted, assuming that the game is already validated.
 * Dates follow the pattern of MM/DD/YYYY.
 */
public class DateGetter implements IGameVisitor<String> {
  @Override
  public String visitWordle(String score) {
    // Split the score string by each space
    String[] splitString = score.split(" ");
    // Day is the second term in the first line of wordle score.
    int wordleDay = Integer.parseInt(splitString[1]);

    // First Wordle was June 19, 2021, with the first game being "Game 0".
    Calendar cal = new GregorianCalendar(2021, Calendar.JUNE, 19);
    // Add the number of days to the calendar and return the new date.
    cal.add(Calendar.DATE, wordleDay);

    int month = cal.get(Calendar.MONTH) + 1; // Month is 0 based index.
    int day = cal.get(Calendar.DATE); // Day is 1 based index.
    int year = cal.get(Calendar.YEAR);

    return month + "/" + day + "/" + year;
  }

  @Override
  public String visitConnections(String score) {
    // Split the score string by each individual line
    String[] splitString = score.split("\n");
    // Day is the third term in the second line of connections score, remove the #.
    int connectionsDay = Integer.parseInt(splitString[1].substring(8));

    // First Connections was June 12, 2023, with the first game being "Game 1".
    Calendar cal = new GregorianCalendar(2023, Calendar.JUNE, 12);
    // Add the number of days to the calendar and return the new date.
    // Subtract 1 since "Game 1" was June 12, 2023.
    cal.add(Calendar.DATE, connectionsDay - 1);

    int month = cal.get(Calendar.MONTH) + 1; // Month is 0 based index.
    int day = cal.get(Calendar.DATE); // Day is 1 based index.
    int year = cal.get(Calendar.YEAR);

    return month + "/" + day + "/" + year;
  }

  @Override
  public String visitMini(String score, boolean isLink) {
    if (isLink) {
      // Date is the 49th-59th indexes, YYYY-MM-DD.
      String year = score.substring(49, 53);
      String month = score.substring(54, 56);
      String date = score.substring(57, 59);
      return month + "/" + date + "/" + year;

    } else {
      // Date is the fourth term is the score.
      // Follows MM/DD/YYYY.
      String[] splitScore = score.split(" ");
      return splitScore[3];
    }
  }

  @Override
  public String visitMurdle(String score) {
    // Date is the second line starting at index 11 (After "Murdle for ").
    // Follows MM/DD/YYYY.
    String[] scoreSplit = score.split("\n");
    return scoreSplit[1].substring(11);
  }
}