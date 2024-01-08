package discordbot.visitor.games;

import discordbot.visitor.visitors.IGameVisitor;

/**
 * IGame implementation for NYT Connections.
 * Calls visitConnections on the visitor.
 */
public class Connections implements IGame {
  private final String score;

  /**
   * Constructor for a Connections game.
   *
   * @param score Potential Connections score to be processed.
   */
  public Connections(String score) {
    this.score = score;
  }

  @Override
  public <T> T accept(IGameVisitor<T> visitor) {
    return visitor.visitConnections(this.score);
  }
}
