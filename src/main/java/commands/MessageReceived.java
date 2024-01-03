package commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import model.ServerSettings;
import model.game.data.FirebaseService;
import model.game.visitor.games.Connections;
import model.game.visitor.games.IGame;
import model.game.visitor.games.Mini;
import model.game.visitor.games.Murdle;
import model.game.visitor.games.Wordle;
import model.game.visitor.visitors.DateGetter;
import model.game.visitor.visitors.ScoreGetter;
import model.game.visitor.visitors.ValidateScore;

public class MessageReceived extends ListenerAdapter {
  private final ServerSettings ss;

  public MessageReceived(ServerSettings ss) {
    this.ss = ss;
  }

  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    // If a bot sent the message then LEAVE!!
    if (event.getAuthor().isBot()) {
      return;
    }

    MessageChannel sentFrom = event.getChannel().asTextChannel();
    MessageChannel whereToMessage =
            this.ss.getDedicatedChannel(event.getGuild(), sentFrom);

    if (sentFrom.equals(whereToMessage)) {
      String msg = event.getMessage().getContentDisplay();
      String gameNameString;
      IGame game;
      if (msg.contains("Connections")) {
        gameNameString = "Connections";
        game = new Connections(msg);
      } else if (msg.contains("Wordle")) {
        gameNameString = "Wordle";
        game = new Wordle(msg);
      } else if (msg.contains("Murdle")) {
        gameNameString = "Murdle";
        game = new Murdle(msg);
      } else if (msg.contains("Mini")) {
        gameNameString = "Mini Not Link";
        game = new Mini(msg, false);
      } else if (msg.contains("mini")) {
        gameNameString = "mini link";
        game = new Mini(msg, true);
      } else {
        return;
      }

      // If it's not a valid score, skip
      if (!game.accept(new ValidateScore())) {
        return;
      }
      Date date = game.accept(new DateGetter());
      String score = game.accept(new ScoreGetter());
      User player = event.getAuthor();

      FirebaseService fbs = new FirebaseService();
      try {
        fbs.saveScore(score, player, gameNameString, date);
      } catch (ExecutionException | InterruptedException e) {
        throw new RuntimeException(e);
      }

      whereToMessage.sendMessage("Game: " + gameNameString + "\nDate: "
              + date + "\nScore:\n" + score).queue();
    }
  }
}
