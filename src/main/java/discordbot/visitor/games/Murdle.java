package discordbot.visitor.games;

import discordbot.visitor.visitors.IGameVisitor;

/**
 * IGame implementation for Murdle by G.T. Karber.
 * Calls visitMurdle on the visitor.
 */
public class Murdle implements IGame {
  private final String score;

  /**
   * Constructor for a Murdle game.
   *
   * @param score Potential Murdle score to be processed.
   */
  public Murdle(String score) {
    this.score = score;
  }

  @Override
  public <T> T accept(IGameVisitor<T> visitor) {
    return visitor.visitMurdle(this.score);
  }
}
