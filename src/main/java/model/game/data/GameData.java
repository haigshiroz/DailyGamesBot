package model.game.data;

import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;

import model.game.GameType;

public class GameData {
  private final Map<Day, Map<GameType, Map<User, String>>> gameData;

  public GameData(Day day, GameType gt, User user, String score) {
    this.gameData = new HashMap<>();
    this.gameData.put(day, new HashMap<>());
    this.gameData.get(day).put(gt, new HashMap<>());
    this.gameData.get(day).get(gt).put(user, score);
  }

  public Map<GameType, Map<User, String>> getDaysScores(Day day) {
    return null;
  }

  public Map<GameType, String> getDaysScoresForPlayer(Day day, User user) {
    return null;
  }

  public Map<User, String> getDaysScoresForGame(Day day, GameType gt) {
    return null;
  }

  public Map<Day, String> getScoresForGameAndPlayer(User user, GameType gt, int days) {
    return null;
  }
}
