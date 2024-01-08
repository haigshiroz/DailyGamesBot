package discordbot.data;

/**
 * Represents the available Games supported by DailyGamesBot.
 */
public enum GameType {
  WORDLE("Wordle"),
  CONNECTIONS("Connections"),
  MINI("Mini"),
  MURDLE("Murdle");

  private final String gameName;

  /**
   * Private constructor for a GameType.
   *
   * @param gameName The String representation of the game.
   */
  GameType(String gameName) {
    this.gameName = gameName;
  }

  @Override
  public String toString() {
    return this.gameName;
  }

  /**
   * Converts the given string to the corresponding GameType object.
   * String must be one of the available GameTypes (case-insensitive).
   *
   * @param gameString String to be converted to a GameType.
   * @return The corresponding GameType based off the given String.
   * @throws IllegalArgumentException If the provided String is not a valid game type.
   */
  public static GameType stringToGameType(String gameString) {
    for (GameType gt : GameType.values()) {
      if (gt.toString().equalsIgnoreCase(gameString)) {
        return gt;
      }
    }

    throw new IllegalArgumentException("Given string is not a valid game type");
  }

  /**
   * Checks whether the given String is a valid game name to be converted to a GameType.
   * A valid game name is once of the available GameTypes.
   *
   * @param gameString String to check whether it is a valid game name to be converted to a GameType.
   * @return Whether the given string is a valid name to be converted to a GameType object.
   */
  public static boolean stringIsGameType(String gameString) {
    boolean ret = false;
    for (GameType gt : GameType.values()) {
      if (gt.toString().equalsIgnoreCase(gameString)) {
        ret = true;
        break;
      }
    }

    return ret;
  }
}
