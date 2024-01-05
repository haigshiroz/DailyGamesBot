package discordbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import discordbot.commands.CompareCommand;
import discordbot.commands.DeleteScoreCommand;
import discordbot.commands.ScanMessages;
import discordbot.commands.StoreGameChannelCommand;
import discordbot.game.data.FirebaseInitializer;

/**
 * Main class holder.
 */
public class DiscordBot {
  /**
   * Main Class.
   *
   * @param args String arguments.
   */
  public static void main(String[] args) {
    FirebaseInitializer.initialize();

    Collection<GatewayIntent> intents = new ArrayList<>();
    intents.add(GatewayIntent.MESSAGE_CONTENT);

    try {
      // Build bot object
      JDA bot = JDABuilder.createDefault(new String(Files.readAllBytes(Paths.get("src/main/resources/discordToken.txt"))))
              .setActivity(Activity.playing("Wordle"))
              .addEventListeners(new StoreGameChannelCommand())
              .addEventListeners(new ScanMessages())
              .addEventListeners(new CompareCommand())
              .addEventListeners(new DeleteScoreCommand())
              .enableIntents(intents)
              .build();

      // Add command options with name, description, and fields.
      bot.updateCommands().addCommands(
              Commands.slash("set-channel", "Sets the text channel where game statistics are stored")
                      .addOption(OptionType.CHANNEL, "channel", "The dedicated game channel.", true),
              Commands.slash("compare", "Shows how other players scored. Defaults to all scores for all games for today.")
                      .addOption(OptionType.STRING, "game", "Specify the game being compared", false)
                      .addOption(OptionType.STRING, "date", "Specify the date in the form of \"MM/DD/YYYY\"", false),
              Commands.slash("delete-score", "Deletes a stored score from a given game and date.")
                      .addOption(OptionType.STRING, "date", "The date the score was for in \"MM/DD/YYYY\".", true)
                      .addOption(OptionType.STRING, "game", "The game the score was for.", true)
      ).queue();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
