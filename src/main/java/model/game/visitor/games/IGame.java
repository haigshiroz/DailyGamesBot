package model.game.visitor.games;

import model.game.visitor.visitors.IGameVisitor;

public interface IGame {
  <T> T accept (IGameVisitor<T> visitor);
}
