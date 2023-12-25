package model.game.visitor.games;

import model.game.visitor.visitors.IGameVisitor;

public class Murdle implements IGame {
  private final String score;

  public Murdle(String score) {
    this.score = score;
  }

  @Override
  public <T> T accept(IGameVisitor<T> visitor) {
    return visitor.visitMurdle(this.score);
  }
}
