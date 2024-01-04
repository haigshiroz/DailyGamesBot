package commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import model.game.GameType;
import model.game.data.FirebaseService;
import model.game.data.ScoreData;
import model.game.visitor.games.Connections;
import model.game.visitor.games.IGame;
import model.game.visitor.games.Mini;
import model.game.visitor.games.Murdle;
import model.game.visitor.games.Wordle;
import model.game.visitor.visitors.DateGetter;
import model.game.visitor.visitors.ScoreGetter;
import model.game.visitor.visitors.ValidateScore;

public class ScanMessages extends ListenerAdapter {
  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    // If a bot sent the message then LEAVE!!
    // Used to narrow down messages scanned for performance.
    if (event.getAuthor().isBot()) {
      return;
    }

    MessageChannel sentFrom = event.getChannel().asTextChannel();
    MessageChannel whereToMessage = FirebaseService.getDedicatedChannel(event.getGuild(), sentFrom);

    if (sentFrom.equals(whereToMessage)) {
      String msg = event.getMessage().getContentDisplay();
      GameType gameType;
      IGame game;
      if (msg.contains("Connections")) {
        gameType = GameType.CONNECTIONS;
        game = new Connections(msg);
      } else if (msg.contains("Wordle")) {
        gameType = GameType.WORDLE;
        game = new Wordle(msg);
      } else if (msg.contains("Murdle")) {
        gameType = GameType.MURDLE;
        game = new Murdle(msg);
      } else if (msg.contains("Mini")) {
        gameType = GameType.MINI;
        game = new Mini(msg, false);
      } else if (msg.contains("mini")) {
        gameType = GameType.MINI;
        game = new Mini(msg, true);
      } else {
        // If the message does not consist a game name, ignore it and return.
        return;
      }

      // If it's not a valid score, ignore the message and return.
      if (!game.accept(new ValidateScore())) {
        return;
      }

      User player = event.getAuthor();
      Date date = game.accept(new DateGetter());
      String score = game.accept(new ScoreGetter());
      Guild server = event.getGuild();
      ScoreData data = new ScoreData(player, gameType, date, score, server);

      try {
        if (!FirebaseService.isDataDuplicate(data)) {
          // Only add data if it is not duplicate.
          FirebaseService.saveScore(data);
          // Confirm message was received, validated, and added to the database by
          // adding a check mark reaction to it.
          event.getMessage().addReaction(Emoji.fromUnicode("\u2705")).queue();
        }
      } catch (ExecutionException | InterruptedException e) {
        whereToMessage.sendMessage("Failed to save data").queue();
      }
    }
  }
}
