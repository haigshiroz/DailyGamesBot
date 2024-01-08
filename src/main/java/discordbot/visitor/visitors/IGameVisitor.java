package discordbot.visitor.visitors;

/**
 * Generic visitor interface that supports each type of game being visited.
 *
 * @param <T> Return type as a result of visiting each type of game.
 */
public interface IGameVisitor<T> {
  /**
   * Visit Wordle.
   *
   * @param message String to be passed and analyzed.
   * @return Generic type. Return depends on interface implementation.
   */
  T visitWordle(String message);

  /**
   * Visit Connections.
   *
   * @param message String to be passed and analyzed.
   * @return Generic type. Return depends on interface implementation.
   */

  T visitConnections(String message);

  /**
   * Visit Mini.
   *
   * @param message String to be passed and analyzed.
   * @param isLink  Whether the message contains a Mini link or not. True means it is a link.
   * @return Generic type. Return depends on interface implementation.
   */
  T visitMini(String message, boolean isLink);

  /**
   * Visit Murdle.
   *
   * @param message String to be passed and analyzed.
   * @return Generic type. Return depends on interface implementation.
   */
  T visitMurdle(String message);
}