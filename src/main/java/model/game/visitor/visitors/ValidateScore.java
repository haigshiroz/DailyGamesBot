package model.game.visitor.visitors;

public class ValidateScore implements IGameVisitor<Boolean> {
  @Override
  public Boolean visitWordle(String score) {
    // Split the score string by each individual line
    String[] splitString = score.split("\n");

    // Count how many lines there are. Minimum score is 1 guess, max is 6 guesses.
    // The header and line gap are two lines, therefore the total must be
    // between 3 and 8 lines.
    int length = splitString.length;
    if (length < 3 || length > 8) {
      return false;
    }

    boolean firstLineValid = this.wordleFirstLineValid(splitString[0]);
    boolean secondLineValid = this.wordleSecondLineValid(splitString[1]);
    boolean thirdPlusLineValid = this.wordleThirdPlusLinesValid(splitString);

    return (firstLineValid && secondLineValid && thirdPlusLineValid);
  }

  private boolean wordleFirstLineValid(String firstLine) {
    // Split first line into the name, the date, and score.
    String[] firstSplit = firstLine.split(" ");
    if (firstSplit.length != 3) {
      return false;
    }

    if (!firstSplit[0].equals("Wordle")) {
      // First word should be "Wordle"
      return false;
    }

    if (!this.IsStringInteger(firstSplit[1])) {
      // Second word should be a number representing the date.
      return false;
    } else {
      int gameNum = Integer.parseInt(firstSplit[1]);
      if (gameNum < 0) {
        // First Wordle started at 0.
        return false;
      }
    }

    // Split the third word into individual characters.
    // First is the number of tries 1-6 or X, second is "/" and third is "6".
    // Optional fourth is "*".
    String firstLineThirdWordFirstLetter = firstSplit[2].substring(0, 1);
    if (this.IsStringInteger(firstLineThirdWordFirstLetter)) {
      int num = Integer.parseInt(firstLineThirdWordFirstLetter);
      if (num < 1 || num > 6) {
        return false;
      }
    } else {
      if (!firstLineThirdWordFirstLetter.equals("X")) {
        return false;
      }
    }

    String firstLineThirdWordRest = firstSplit[2].substring(1);
    if (!firstLineThirdWordRest.equals("/6") && !firstLineThirdWordRest.equals("/6*")) {
      // Rest of the string must be "/6" or "/6*".
      return false;
    }

    return true;
  }

  private boolean wordleSecondLineValid(String secondLine) {
    return secondLine.isEmpty();
  }

  private boolean wordleThirdPlusLinesValid(String[] splitString) {
    for (int lineNum = 2; lineNum < splitString.length; lineNum++) {
      // Obtain each line past the second line.
      String line = splitString[lineNum];

      // Keep track of how many characters were counted.
      // Unicode can consist of multiple single-strings.
      // Black squares are one single-strings while green and yellow squares are two.
      int numCharsCounted = 0;

      // Iterate through each single string in the line.
      for (int stringIndex = 0; stringIndex < line.length(); stringIndex++) {
        String letter = line.substring(stringIndex, stringIndex + 1);
        if (letter.compareTo("\uD83D") == 0) {
          // Get next and check it's "\uDFE8" or "\uDFE9" (yellow or green squares).
          // Since yellow and green are two, increase the string index.
          stringIndex++;
          if (stringIndex >= line.length()) {
            // Make sure the next string index is in bounds.
            return false;
          }
          // Obtain the next letter.
          String nextLetter = line.substring(stringIndex, stringIndex + 1);
          if (nextLetter.compareTo("\uDFE8") != 0 && nextLetter.compareTo("\uDFE9") != 0) {
            // Check if the code matches that of yellow or green squares.
            return false;
          }
        } else if (letter.compareTo("\u2B1B") != 0) {
          // Check if the code is that of a black square.
          return false;
        }

        // Add to the number of characters counted.
        numCharsCounted++;
      }

      if (numCharsCounted != 5) {
        // Each wordle guess is five letters.
        return false;
      }
    }

    return true;
  }

  @Override
  public Boolean visitConnections(String score) {
    // Split the score string by each individual line
    String[] splitString = score.split("\n");

    // Count how many lines there are.
    // Minimum score is 4 right guesses, max is 7 (3 right/wrong and 4 wrong/right) guesses.
    // The header is two lines therefore the total string must be
    // between 6 and 9 lines.
    int length = splitString.length;
    if (length < 6 || length > 9) {
      return false;
    }

    boolean firstLineValid = this.connectionsFirstLineValid(splitString[0]);
    boolean secondLineValid = this.connectionsSecondLineValid(splitString[1]);
    boolean thirdPlusLineValid = this.connectionsThirdPlusLinesValid(splitString);

    return (firstLineValid && secondLineValid && thirdPlusLineValid);
  }

  private boolean connectionsFirstLineValid(String firstLine) {
    return firstLine.equals("Connections ");
  }

  private boolean connectionsSecondLineValid(String secondLine) {
    // Second line is "Puzzle #[number]", minimum length is 9.
    if (secondLine.length() < 9) {
      return false;
    }

    // Static half will always be "Puzzle #".
    String staticHalf = secondLine.substring(0, 8);
    // Dynamic half will always be a non-negative number.
    String dynamicHalf = secondLine.substring(8);

    boolean staticValid = staticHalf.equals("Puzzle #");
    boolean dynamicValid;

    if (!this.IsStringInteger(dynamicHalf)) {
      return false;
    } else {
      int num = Integer.parseInt(dynamicHalf);
      dynamicValid = num >= 0;
    }

    return staticValid && dynamicValid;
  }

  private boolean connectionsThirdPlusLinesValid(String[] splitString) {
    // Iterate through each line after the header to make sure they are scores.
    for (int lineNum = 2; lineNum < splitString.length; lineNum++) {
      // For yellow, green, blue, and purple squares the unicode is two single strings.
      // Each guess is four squares so the total length must be 8.
      String line = splitString[lineNum];

      if (line.length() != 8) {
        return false;
      }

      // Iterate through each single string in the line.
      // Since each unicode for the squares are 2 single strings, increase by 2.
      for (int stringIndex = 0; stringIndex <= 6; stringIndex += 2) {
        String first = line.substring(stringIndex, stringIndex + 1);
        String second = line.substring(stringIndex + 1, stringIndex + 2);
        boolean firstValid = first.compareTo("\uD83D") == 0;
        boolean secondValid = second.compareTo("\uDFE8") == 0 || second.compareTo("\uDFE9") == 0 || second.compareTo("\uDFEA") == 0 || second.compareTo("\uDFE6") == 0;

        // If both are not valid for each line, return false.
        if (!(firstValid && secondValid)) {
          return false;
        }
      }
    }

    return true;
  }

  @Override
  public Boolean visitMini(String score, boolean isLink) {
    if (isLink) {
      return this.validateMiniLink(score);
    } else {
      return this.validateMiniNoLink(score);
    }
  }

  private boolean validateMiniLink(String score) {
    // Sample Links:
    // https://www.nytimes.com/badges/games/mini.html?d=2023-12-20&t=16&c=3fd...
    // https://www.nytimes.com/badges/games/mini.html?d=2023-12-04&t=65&c=ed8...
    if (score.length() <= 68) {
      return false;
    }
    // Should be "https://www.nytimes.com/badges/games/mini.html?d=".
    String staticHalf = score.substring(0, 49);
    // Should be "YYYY-MM-DD&t=SS(S)&c=..."
    String dynamicHalf = score.substring(49);

    boolean staticValid = staticHalf.equals("https://www.nytimes.com/badges/games/mini.html?d=");
    boolean dynamicValid;
    String date = dynamicHalf.substring(0, 10);
    String[] dateSplit = date.split("-");
    if (dateSplit.length != 3) {
      // Should be three terms (year-month-date)
      return false;
    }
    if (!this.IsStringInteger(dateSplit[0] + dateSplit[1] + dateSplit[2])) {
      // The three terms should all be integers (year, month, date)
      return false;
    }
    int year = Integer.parseInt(dateSplit[0]);
    int month = Integer.parseInt(dateSplit[1]);
    int day = Integer.parseInt(dateSplit[2]);
    boolean dateValid = year >= 2014 && month >= 1 && month <= 12 && day >= 1 && day <= 31;
    String afterDateBeforeTime = dynamicHalf.substring(10, 13);
    boolean afterdateBeforeTimeValid = afterDateBeforeTime.equals("&t=");
    // Time can be an arbitrary number of digits, just check that there is at least one digit > 0.
    String time = dynamicHalf.substring(13, 14);
    if (!this.IsStringInteger(time)) {
      return false;
    }
    boolean timeValid = Integer.parseInt(time) > 0;

    String timeAndAfter = dynamicHalf.substring(13);
    boolean afterTimeValid = timeAndAfter.contains("&c=");

    dynamicValid = dateValid && afterdateBeforeTimeValid && timeValid && afterTimeValid;

    return staticValid && dynamicValid;
  }

  private boolean validateMiniNoLink(String score) {
    // Makeup of score is "I solved the " + [date] + " New York Times Mini Crossword in "
    // + time + "! https://www.nytimes.com/crosswords/game/mini".
    String[] scoreSplit = score.split(" ");
    if (scoreSplit.length != 12) {
      return false;
    }

    // "I solved the"
    String firstSegment = scoreSplit[0] + scoreSplit[1] + scoreSplit[2];
    // "M(M)/D(D)/YYYY"
    String dateSegment = scoreSplit[3];
    // "New York Times Mini Crossword in"
    String thirdSegment = scoreSplit[4] + scoreSplit[5] + scoreSplit[6] + scoreSplit[7] + scoreSplit[8] + scoreSplit[9];
    // "M(M...):SS!"
    String timeSegment = scoreSplit[10];
    // "https://www.nytimes.com/crosswords/game/mini"
    String fifthSegment = scoreSplit[11];

    boolean firstValid = firstSegment.equals("Isolvedthe");

    boolean dateValid;
    String[] splitDate = dateSegment.split("/");
    if (splitDate.length != 3) {
      // Must have three terms (month, day, year).
      return false;
    }
    if (!this.IsStringInteger(splitDate[0] + splitDate[1] + splitDate[2])) {
      // The three terms must be integers.
      return false;
    }
    int month = Integer.parseInt(splitDate[0]);
    int day = Integer.parseInt(splitDate[1]);
    int year = Integer.parseInt(splitDate[2]);
    dateValid = month >= 1 && month <= 12 && day >= 1 && day <= 31 && year >= 2014;

    boolean thirdValid = thirdSegment.equals("NewYorkTimesMiniCrosswordin");

    boolean timeValid;
    String[] timeSplit = timeSegment.split(":");
    if (timeSplit.length != 2) {
      // Time must be two segments (min, sec + !).
      return false;
    }
    if (timeSplit[1].length() != 3) {
      // Second segment must be of length 3 (two digit seconds + !).
      return false;
    }
    if (!this.IsStringInteger(timeSplit[0]) || !this.IsStringInteger(timeSplit[1].substring(0, 2))) {
      // Both terms must be integers (minus the ! of the second term).
      return false;
    }
    int min = Integer.parseInt(timeSplit[0]);
    int sec = Integer.parseInt(timeSplit[1].substring(0, 2));
    timeValid = min >= 0 && sec >= 0 && sec <= 59 && timeSplit[1].substring(2).equals("!");

    boolean fifthValid = fifthSegment.equals("https://www.nytimes.com/crosswords/game/mini");

    return firstValid && dateValid && thirdValid && timeValid && fifthValid;
  }

  @Override
  public Boolean visitMurdle(String score) {
    boolean valid = true;
    // Layout:
    // First line: title, all caps.
    // Second line: Murdle for MM/DD/YYYY.
    // Third line empty.
    // Fourth line "\uD83D\uDC64\uD83D\uDD2A\uD83C\uDFE1     \uD83D\uDD70\uFE0F".
    // Fifth line "\emoji\emoji\emoji     \emoji0-9:\emoji0-5\emoji0-9".
    // Sixth lien empty.
    // Seventh line "⚖".
    // Eighth is a bunch of emojis.
    // Ninth-Eleventh lines empty.
    // Twelfth line https://murdle.com.
    String[] scoreSplit = score.split("\n");

    // Check all the empty rows first. Third, Sixth, Ninth, Tenth, and Eleventh lines are empty.
    // Indexes are 2, 5, 8, 9, 10.
    int[] emptyLines = {2, 5, 8, 9, 10};
    for (int ind : emptyLines) {
      // For every empty line index, if the message is still valid, check if the line is empty.
      // Else, break since the message is invalid.
      valid = scoreSplit[ind].isEmpty();

      if(!valid) {
        break;
      }
    }

    // First line title should be all caps (and potentially other symbols).
    if (valid) {
      for (char c : scoreSplit[0].toCharArray()) {
        if (Character.isAlphabetic(c) && !Character.isUpperCase(c)) {
          valid = false;
          break;
        }
      }
    }

    // Second line is "Murdle for " + "MM/DD/YYYY".
    if (valid) {
      valid = this.murdleValidSecondLine(scoreSplit[1]);
    }

    // Fourth line should be Silhouette, Knife, House, five spaces, Clock.
    if (valid) {
      String expectedFourthLine = "\uD83D\uDC64\uD83D\uDD2A\uD83C\uDFE1     \uD83D\uDD70\uFE0F";
      valid = scoreSplit[3].compareTo(expectedFourthLine) == 0;
    }

    // Fifth line is 3 emojis that are either check of x, five spaces, then the time.
    // Time is any number of emojis 0-9, ":", then two emojis 0-9.
    if (valid) {
      valid = this.murdleValidFifthLine(scoreSplit[4]);
    }

    // Seventh line should be Scale emoji.
    if (valid) {
      if (scoreSplit[6].compareTo("⚖\uFE0F") != 0) {
        valid = false;
      }
    }

    // Eighth line is String of emojis.
    // Can't check eighth line too thoroughly.
    if (valid) {
      if (scoreSplit[7].contains(" ")) {
        valid = false;
      }
    }

    // Eleventh line should be "https://murdle.com/" or "https://murdle.com".
    if (valid) {
      if (!scoreSplit[11].equals("https://murdle.com/") && !scoreSplit[11].equals("https://murdle.com")) {
        valid = false;
      }
    }


    return valid;
  }

  private boolean murdleValidSecondLine(String secondLine) {
    boolean valid = true;

    // Second line is "Murdle for " + "MM/DD/YYYY".
    String[] lineTwoSplit = secondLine.split(" ");

    if (lineTwoSplit.length != 3) {
      valid = false;
    }

    if (valid) {
      // First two words of the second line must be "Murdle" and "for".
      if (!(lineTwoSplit[0] + lineTwoSplit[1]).equals("Murdlefor")) {
        valid = false;
      }

      // Third word should be a valid MM/DD/YYYY.
      String[] dateSplit = lineTwoSplit[2].split("/");
      if (valid && !this.IsStringInteger(dateSplit[0] + dateSplit[1] + dateSplit[2])) {
        // Date should be integers.
        valid = false;
      }

      if (valid) {
        int month = Integer.parseInt(dateSplit[0]);
        int day = Integer.parseInt(dateSplit[1]);
        int year = Integer.parseInt(dateSplit[2]);

        if (!(month >= 1 && month <= 12 && day >= 1 && day <= 31 && year >= 2022)) {
          // Month and day must be valid, first Murdle was in 2022 (?).
          valid = false;
        }
      }
    }

    return valid;
  }

  private boolean murdleValidFifthLine(String fifthLine) {
    boolean valid = true;

    // Fifth line is 3 emojis that are either check of x, five spaces, then the time.
    // Time is any number of emojis 0-9, ":", then two emojis 0-9.
    if (fifthLine.length() < 12) {
      // Minimum length must be 12 (three emojis, five spaces, emoji, :, two emojis).
      valid = false;
    } else {
      String firstThird = fifthLine.substring(0, 3);
      String secondThird = fifthLine.substring(3, 8);
      String thirdThird = fifthLine.substring(8);

      boolean firstValid = true;
      for (String s : firstThird.split("")) {
        if (!s.equals("\u2705") && !s.equals("\u274C")) {
          // First segment must contain only green check or red x emoji.
          firstValid = false;
          break;
        }
      }

      // Second segment is five spaces.
      boolean secondValid = secondThird.equals("     ");

      // Third segment is any positive amount of number emojis, ":", then two number emojis.
      boolean thirdValid = true;
      String[] thirdSplit = thirdThird.split(":");
      if (thirdSplit.length != 2) {
        // Third is numbers, ":", numbers.
        thirdValid = false;
      } else {
        // First segment should be positive length, second segment should be of length 2.
        // Since number emojis are a number + \uFE0F +  ⃣ , the length is 3 for each emoji.
        // Thus, the first segment must be a positive multiple of 3 and the second should
        // be of length 6.
        if (thirdSplit[0].isEmpty() || (thirdSplit[0].length() % 3) != 0 || thirdSplit[1].length() != 6) {
          thirdValid = false;
        }
      }
      // Given that so far the third line is the right size, check if the String is only number
      // emojis.
      if (thirdValid) {
        // Third segment without ":".
        String thirdCombined = thirdSplit[0] + thirdSplit[1];

        // Go by 3's since each number emoji is strings of length of three.
        for (int strInd = 0; strInd < thirdCombined.length(); strInd += 3) {
          String tempStr = thirdCombined.substring(strInd, strInd + 3);
          if(!(this.IsStringInteger(tempStr.substring(0, 1)) && tempStr.substring(1).compareTo("\uFE0F\u20E3") == 0)) {
            // Each group of three single Strings must be a number emoji.
            thirdValid = false;
            break;
          }
        }
      }

      if (!(firstValid && secondValid && thirdValid)) {
        // If all three parts of the fifth line are not valid, the validity is false.
        valid = false;
      }
    }

    return valid;
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
