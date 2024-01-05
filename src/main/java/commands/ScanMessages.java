package commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Date;
import java.util.List;
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
      try {
        // Obtain ScoreData object from message event.
        // May through IllegalArgumentException if message is not a valid game.
        ScoreData data = this.retrieveDataFromMessage(event.getMessage());
        // Upload data to database
        this.addScoreDatatoDatabase(data, event.getMessage());
      } catch (IllegalArgumentException e) {
        // If the message is not a valid game, ignore it.
      }
    }
  }

  @Override
  public void onMessageReactionAdd(MessageReactionAddEvent event) {
    // Criteria for user confirming to update their data:
    // 1. Reacter wasn't this bot
    // 2. Reacter ID is the same as the sender ID.
    // 3. Bot reacted to the message with both refresh and question mark emojis.
    // 4. User's reaction is the refresh emoji.

    // User object of DailyGamesBot.
    String botID = "1172328575299485786";
    User botUser = event.getGuild().retrieveMemberById(botID).complete().getUser();

    // Refresh question mark, and check mark emojis.
    Emoji refresh = Emoji.fromUnicode("\uD83D\uDD04");
    Emoji questionMark = Emoji.fromUnicode("\u2753");
    Emoji checkMark = Emoji.fromUnicode("\u2705");

    // User who added the reaction.
    User reacter = event.getUser();
    // ID of user who sent the message that was reacted to.
    String senderID = event.getMessageAuthorId();
    // Message that was sent.
    Message message = event.retrieveMessage().complete();
    // List of users who reacted refresh and question mark.
    List<User> refreshReacters = event.getChannel().retrieveReactionUsersById(message.getId(), refresh).complete();
    List<User> questionReacters = event.getChannel().retrieveReactionUsersById(message.getId(), questionMark).complete();

    if (reacter != null && !reacter.equals(botUser) &&
            reacter.getId().equals(senderID) &&
            refreshReacters.contains(botUser) && questionReacters.contains(botUser) &&
            event.getEmoji().equals(refresh)) {
      // User wishes to update data.
      // Remove reactions from the bot, delete data, add data to database.
      message.addReaction(checkMark).queue();
      message.removeReaction(refresh).queue();
      message.removeReaction(questionMark).queue();

      ScoreData data = this.retrieveDataFromMessage(message);
      FirebaseService.deleteScore(data);
      this.addScoreDatatoDatabase(data, message);
    }
  }

  private ScoreData retrieveDataFromMessage(Message message) {
    String msg = message.getContentDisplay();
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
      throw new IllegalArgumentException("Message is not valid game");
    }

    // If it's not a valid score, ignore the message and return.
    if (!game.accept(new ValidateScore())) {
      throw new IllegalArgumentException("Message is not valid game");
    }

    User player = message.getAuthor();
    Date date = game.accept(new DateGetter());
    String score = game.accept(new ScoreGetter());
    Guild server = message.getGuild();
    return new ScoreData(player, gameType, date, score, server);
  }

  private void addScoreDatatoDatabase(ScoreData data, Message message) {
    // Upload data to database if not already resent.
    // "Duplicate" refers to same date, game, server, and user, not same score.
    // Users have an option to update score.
    try {
      if (!FirebaseService.isDataDuplicate(data)) {
        // Only add data automatically if it is not duplicate.
        FirebaseService.saveScore(data);
        // Confirm message was received, validated, and added to the database by
        // adding a check mark reaction to it.
        message.addReaction(Emoji.fromUnicode("\u2705")).queue();
      } else {
        // If it's a duplicate, ask to add data.
        // React with a :arrows_counterclockwise: emoji and :question: emoji to
        // ask if the user would like to refresh the data.
        // If they react (with either), then refresh it.
        message.addReaction(Emoji.fromUnicode("\uD83D\uDD04")).queue();
        message.addReaction(Emoji.fromUnicode("\u2753")).queue();
      }
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
      message.getChannel().sendMessage("Failed to save data").queue();
    }
  }
}