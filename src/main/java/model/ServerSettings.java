package model;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.Interaction;

import java.util.HashMap;
import java.util.Map;

/**
 * Keeps track of server settings such as dedicated channels for games.
 */
public class ServerSettings {
  private final Map<Guild, IndividualSettings> settingsList;

  public ServerSettings() {
    this.settingsList = new HashMap<>();
  }

  /**
   * Adds the given server to the list of servers mapped to settings.
   * @param server Server being added.
   */
  public void addServer(Guild server) {
    System.out.println("Adding server: " + server.getName());
    // Check if that server is already there
    if(this.settingsList.containsKey(server)) {
      System.out.println(server.getName() + " is already added");
      return;
    }

    System.out.println("Server added");
    this.settingsList.put(server, new IndividualSettings());
  }

  /**
   * Sets the channel that the bot will use for communications.
   * @param server Server that this channel is being set for.
   * @param channel Channel being set.
   */
  public void setDedicatedChannel(Guild server, MessageChannel channel) {
    this.settingsList.get(server).setDedicatedChannel(channel);
  }

  /**
   * Returns the dedicated game message channel if set. If not, then returns the channel
   * where the interaction occurred.
   * @param event Event that occurred.
   * @return A suitable message channel.
   */
  public MessageChannel getDedicatedChannel(Interaction event) {
    Guild server = event.getGuild();
    IndividualSettings is = this.settingsList.get(server);
    MessageChannel channel = is.getDedicatedChannel();
    if(channel == null) {
      return event.getMessageChannel();
    }
    return channel;
  }

  public MessageChannel getDedicatedChannel(Guild server, MessageChannel backup) {
    IndividualSettings is = this.settingsList.get(server);
    MessageChannel channel = is.getDedicatedChannel();
    if(channel == null) {
      return backup;
    }
    return channel;
  }


  /**
   * Settings data for each individual server
   */
  private class IndividualSettings {
    /**
     * Channel where bot communicates.
     */
    private MessageChannel dedicatedChannel;


    /**
     * No-arg contructor for IndividualSettings
     */
    private IndividualSettings() {
      this.dedicatedChannel = null;
    }

    private void setDedicatedChannel(MessageChannel channel) {
      this.dedicatedChannel = channel;
    }

    private MessageChannel getDedicatedChannel() {
      return this.dedicatedChannel;
    }
  }
}
