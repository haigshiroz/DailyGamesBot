package discordbot.game.visitor.games;

import discordbot.game.visitor.visitors.IGameVisitor;

public class Connections implements IGame {
  private final String score;
  public Connections(String score) {
    this.score = score;
  }
  @Override
  public <T> T accept(IGameVisitor<T> visitor) {
    return visitor.visitConnections(this.score);
  }
}
