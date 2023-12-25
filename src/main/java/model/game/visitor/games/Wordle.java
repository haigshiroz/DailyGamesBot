package model.game.visitor.games;

import model.game.visitor.visitors.IGameVisitor;

public class Wordle implements IGame {
  private final String score;
  public Wordle(String score) {
    this.score = score;
  }
  @Override
  public <T> T accept(IGameVisitor<T> visitor) {
    return visitor.visitWordle(this.score);
  }
}