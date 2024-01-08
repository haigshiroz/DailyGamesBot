package discordbot.visitor.visitors;

/**
 * Implementation of the IGameVisitor class of type String.
 * Precondition: Given String is already validated to be a copy-paste for the
 * respective game.
 * Obtains the score of each game (time, number of tries, accuracy, etc.) as a String.
 */
public class ScoreGetter implements IGameVisitor<String> {
  @Override
  public String visitWordle(String score) {
    // Split the score string by each individual line.
    String[] splitString = score.split("\n");
    // Split first line by spaces to get the score number.
    String[] splitStringFirstLine = splitString[0].split(" ");

    // String we are returning as the score.
    String returningScore = "";

    // Add the number score.
    returningScore += splitStringFirstLine[2] + "\n";

    // Add the guesses score.
    for (int lineNum = 2; lineNum < splitString.length - 1; lineNum++) {
      returningScore += splitString[lineNum] + "\n";
    }
    returningScore += splitString[splitString.length - 1];

    return returningScore;
  }

  @Override
  public String visitConnections(String score) {
    // Split the score string by each individual line.
    String[] splitString = score.split("\n");

    // String we are returning as the score.
    String returningScore = "";

    // Add the guesses score.
    for (int lineNum = 2; lineNum < splitString.length - 1; lineNum++) {
      returningScore += splitString[lineNum] + "\n";
    }
    returningScore += splitString[splitString.length - 1];

    return returningScore;
  }

  @Override
  public String visitMini(String score, boolean isLink) {
    if (isLink) {
      String timeAndAfter = score.substring(62);
      String time = "";
      // Loop for every single string at the time. Once a non-integer is reached, break.
      for (int strInd = 0; true; strInd++) {
        String str = timeAndAfter.substring(strInd, strInd + 1);
        if (ValidateScore.isStringInteger(str)) {
          time += str;
        } else {
          break;
        }
      }

      return time + " seconds";
    } else {
      String[] splitScore = score.split(" ");
      String time = splitScore[10];
      return time.substring(0, time.length() - 1) + "seconds";
    }
  }

  @Override
  public String visitMurdle(String score) {
    // Line 4 (index 3) describes the person, weapon, setting, and time.
    // Line 5 (index 4) is the accuracy of the person, weapon, and setting and the time.
    // Line 7 (index 6) is how many hints were used. If none, the line and the line before is not present.
    // Line 7 (index 6) (no hint) or Line 9 (index 8) (hint) describes the streak.
    // Line 8 (index 7) (no hint) or Line 10 (index 9) (hint) is the streak.
    String[] scoreSplit = score.split("\n");
    String ret = "";
    if (scoreSplit.length == 12) {
      // No hint used
      ret += scoreSplit[3] + "\n" + scoreSplit[4] + "\n" + scoreSplit[6] + "\n" + scoreSplit[7];
    } else {
      // Hint was used
      ret += scoreSplit[3] + "\n" + scoreSplit[4] + "\n" + scoreSplit[6] + "\n" + scoreSplit[8] + "\n" + scoreSplit[9];
    }
    return ret;
  }
}