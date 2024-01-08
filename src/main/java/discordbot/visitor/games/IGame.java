package discordbot.visitor.games;

import discordbot.visitor.visitors.IGameVisitor;

/**
 * Represents a supported game.
 * Function object that takes in a visitor and depending on the game implementation,
 * calls that game's visitor visit method.
 */
public interface IGame {
  /**
   * Accepts the IGameVisitor object and depending on the class implementation,
   * visit that visitor's visit method of the same game.
   *
   * @param visitor Visitor object to call visit on.
   * @param <T>     Type being returned.
   * @return The return type of the visitor.
   */
  <T> T accept(IGameVisitor<T> visitor);
}
