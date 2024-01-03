import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.ArrayList;
import java.util.Collection;

import commands.EchoCommand;
import commands.MessageReceived;
import commands.StoreGameChannelCommand;
import commands.WordleCommand;
import model.ServerSettings;
import model.game.data.FirebaseInitializer;

/**
 * Main class holder.
 */
public class DiscordBot {
  /**
   * Main Class.
   * @param args String arguments.
   */
  public static void main(String[] args) throws InterruptedException {
    // Server Settings
    ServerSettings ss = new ServerSettings();
    Collection<GatewayIntent> intents = new ArrayList<>();
    intents.add(GatewayIntent.MESSAGE_CONTENT);

    // Build bot object
    JDA bot = JDABuilder.createDefault("MTE3MjMyODU3NTI5OTQ4NTc4Ng.Gp3QUe.o5pr8BZIg0oMs1cu_xbaCy1Qt3xQ4Bz7oBA8J4")
            .setActivity(Activity.watching("Test"))
            .addEventListeners(new EchoCommand(ss))
            .addEventListeners(new WordleCommand(ss))
            .addEventListeners(new StoreGameChannelCommand(ss))
            .enableIntents(intents)
            .addEventListeners(new MessageReceived(ss))
            .build();

    // Add command options with name, description, and fields.
    bot.updateCommands().addCommands(
            Commands.slash("echo", "Repeats message back to you")
                    .addOption(OptionType.STRING, "message", "The message being repeated", true),
            Commands.slash("wordle", "Tracks your Wordle score.")
                    .addOption(OptionType.STRING, "score", "Your Wordle score.", true),
            Commands.slash("set-channel", "Sets the text channel where game statistics are stored")
                    .addOption(OptionType.CHANNEL, "channel", "The dedicated game channel.", true)
    ).queue();


    // Track the servers this bot is in
    for(Guild s : bot.awaitReady().getGuilds()) {
      ss.addServer(s);
    }


    FirebaseInitializer fbi = new FirebaseInitializer();
    fbi.initialize();
  }
}
