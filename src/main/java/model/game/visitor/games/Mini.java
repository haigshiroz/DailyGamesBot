package model.game.visitor.games;

import model.game.visitor.visitors.IGameVisitor;

public class Mini implements IGame {
  private final String score;
  private final boolean isLink;
  public Mini(String score, boolean isLink) {
    this.score = score;
    this.isLink = isLink;
  }
  @Override
  public <T> T accept(IGameVisitor<T> visitor) {
    return visitor.visitMini(this.score, this.isLink);
  }
}
