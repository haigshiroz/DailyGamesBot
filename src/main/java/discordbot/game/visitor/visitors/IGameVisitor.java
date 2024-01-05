package discordbot.game.visitor.visitors;

public interface IGameVisitor<T> {
  T visitWordle(String score);

  T visitConnections(String score);

  T visitMini(String score, boolean isLink);

  T visitMurdle(String score);
}