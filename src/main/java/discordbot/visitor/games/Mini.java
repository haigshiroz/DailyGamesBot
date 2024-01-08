package discordbot.visitor.games;

import discordbot.visitor.visitors.IGameVisitor;

/**
 * IGame implementation for NYT Mini.
 * Calls visitMini on the visitor.
 */
public class Mini implements IGame {
  private final String score;
  private final boolean isLink;

  /**
   * Constructor for a Mini game.
   *
   * @param score  Potential Mini score to be processed.
   * @param isLink Whether the score is a link or not. True means it is a link.
   */
  public Mini(String score, boolean isLink) {
    this.score = score;
    this.isLink = isLink;
  }

  @Override
  public <T> T accept(IGameVisitor<T> visitor) {
    return visitor.visitMini(this.score, this.isLink);
  }
}
