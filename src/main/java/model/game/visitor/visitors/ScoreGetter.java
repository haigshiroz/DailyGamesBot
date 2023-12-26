package model.game.visitor.visitors;

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
      for(int strInd = 0; true; strInd++) {
        String str = timeAndAfter.substring(strInd, strInd + 1);
        if(this.IsStringInteger(str)) {
          time += str;
        } else {
          break;
        }
      }

      return time;
    } else {
      String[] splitScore = score.split(" ");
      String time = splitScore[10];
      return time.substring(0, time.length()-1);
    }
  }

  @Override
  public String visitMurdle(String score) {
    // Line 4 (index 3) describes the person, weapon, setting, and time.
    // Line 5 (index 4) is the accuracy of the person, weapon, and setting and the time.
    // Line 7 (index 6) describes the streak.
    // Line 8 (index 7) is the streak.
    String[] scoreSplit = score.split("\n");
    return scoreSplit[3] + "\n" + scoreSplit[4] + "\n" +scoreSplit[6] + "\n" +scoreSplit[7];
  }

  private boolean IsStringInteger(String str) {
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