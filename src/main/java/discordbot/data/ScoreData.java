package discordbot.data;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.util.Date;

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

  /**
   * Constructor for a ScoreData object.
   *
   * @param player     Discord User object that submitted the score.
   * @param game       GameType that the score was for.
   * @param dateOfgame Date that the score was for.
   * @param score      String score representing how the player did.
   * @param server     Discord Guild object that the score was submitted for.
   * @throws IllegalArgumentException if any of the arguments are null.
   */
  public ScoreData(User player, GameType game, Date dateOfgame, String score, Guild server) {
    if (player == null || game == null || dateOfgame == null || score == null || server == null) {
      throw new IllegalArgumentException("Arguments must not be null.");
    }

    this.player = player;
    this.game = game;
    this.dateOfgame = dateOfgame;
    this.score = score;
    this.server = server;
  }

  /**
   * Gets this ScoreData's User object.
   *
   * @return This ScoreData's User object.
   */
  public User getPlayer() {
    return this.player;
  }

  /**
   * Gets this ScoreData's GameType object.
   *
   * @return This ScoreData's GameType object.
   */
  public GameType getGame() {
    return this.game;
  }

  /**
   * Gets this ScoreData's Date object.
   *
   * @return This ScoreData's Date object.
   */
  public Date getDate() {
    return this.dateOfgame;
  }

  /**
   * Gets this ScoreData's score as a String.
   *
   * @return This ScoreData's score as a String.
   */
  public String getScore() {
    return this.score;
  }

  /**
   * Gets this ScoreData's Guild object.
   *
   * @return This ScoreData's Guild object.
   */
  public Guild getServer() {
    return this.server;
  }
}