package model.game.data;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.util.Date;

import model.game.GameType;

/**
 * Represents a data object that stores all relevant details for a game including the
 * player's username, the game being played, the date of the game, the score, and the server.
 */
public class ScoreData {
  private final User player;
  private final GameType game;
  private final Date dateOfgame;
  private final String score;
  private final Guild server;

  public ScoreData(User player, GameType game, Date dateOfgame, String score, Guild server) {
    this.player = player;
    this.game = game;
    this.dateOfgame = dateOfgame;
    this.score = score;
    this.server = server;
  }

  public User getPlayer() {
    return this.player;
  }

  public GameType getGame() {
    return this.game;
  }

  public Date getDate() {
    return this.dateOfgame;
  }

  public String getScore() {
    return this.score;
  }

  public Guild getServer() {
    return this.server;
  }
}
