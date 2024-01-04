package model.game;

public enum GameType {
  WORDLE("Wordle"),
  CONNECTIONS("Connections"),
  MINI("Mini"),
  MURDLE("Murdle");

  private final String gameName;

  GameType(String gameName) {
    this.gameName = gameName;
  }

  @Override
  public String toString() {
    return this.gameName;
  }

  public static GameType stringToGameType(String name) {
    for(GameType gt : GameType.values()) {
      if(gt.toString().equalsIgnoreCase(name)) {
        return gt;
      }
    }

    throw new IllegalArgumentException("Given string is not a valid game type");
  }

  public static boolean stringIsGameType(String name) {
    for(GameType gt : GameType.values()) {
      if(gt.toString().equalsIgnoreCase(name)) {
        return true;
      }
    }

    return false;
  }
}
