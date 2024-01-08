package discordbot.visitor.games;

import discordbot.visitor.visitors.IGameVisitor;

/**
 * IGame implementation for NYT Wordle.
 * Calls visitWordle on the visitor.
 */
public class Wordle implements IGame {
  private final String score;

  /**
   * Constructor for a Wordle game.
   *
   * @param score Potential Wordle score to be processed.
   */
  public Wordle(String score) {
    this.score = score;
  }

  @Override
  public <T> T accept(IGameVisitor<T> visitor) {
    return visitor.visitWordle(this.score);
  }
}
