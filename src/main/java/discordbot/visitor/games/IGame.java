package discordbot.game.visitor.games;

import discordbot.game.visitor.visitors.IGameVisitor;

public interface IGame {
  <T> T accept (IGameVisitor<T> visitor);
}
