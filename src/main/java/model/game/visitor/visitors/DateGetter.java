package model.game.visitor.visitors;

public class DateGetter implements IGameVisitor<String> {
  @Override
  public String visitWordle(String score) {
    // Split the score string by each space
    String[] splitString = score.split(" ");
    // Day is the second term in the first line of wordle score.
    return splitString[1];
  }

  @Override
  public String visitConnections(String score) {
    // Split the score string by each individual line
    String[] splitString = score.split("\n");
    // Day is the third term in the second line of connections score, remove the #.
    return splitString[1].substring(8);
  }

  @Override
  public String visitMini(String score, boolean isLink) {
    if(isLink) {
      // Date is the 49th-59th indexes.
      return score.substring(49, 59);

    } else {
      // Date is the fourth term is the score.
      String[] splitScore = score.split(" ");
      return splitScore[3];
    }
  }

  @Override
  public String visitMurdle(String score) {
    return "Murdle Date";
  }
}