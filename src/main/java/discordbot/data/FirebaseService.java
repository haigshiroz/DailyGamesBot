package discordbot.game.data;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import discordbot.game.GameType;

public class FirebaseService {
  public static void saveScore(ScoreData data) throws ExecutionException, InterruptedException {
    // Add all the data to a Map (Firebase accepts Maps as data).
    // Keys are fields and values are the data representing that field.
    Map<String, String> dataSubmission = new HashMap<>();
    dataSubmission.put("Date", data.getDate().toString());
    dataSubmission.put("Game", data.getGame().toString());
    dataSubmission.put("Player", data.getPlayer().getName());
    dataSubmission.put("Score", data.getScore());
    dataSubmission.put("Server", data.getServer().getId());

    // Upload data to database.
    Firestore dataBase = FirestoreClient.getFirestore();
    dataBase.collection("gameScores").document().set(dataSubmission);
  }

  public static boolean isDataDuplicate(ScoreData data) {
    // Boolean returning whether the data is a duplicate.
    boolean ret = true;

    // Filters to check if the game data was already in the database.
    // Duplicates have the same date, game, player, and server. Not the same score.
    Filter filterDate = Filter.equalTo("Date", data.getDate().toString());
    Filter filterGame = Filter.equalTo("Game", data.getGame().toString());
    Filter filterPlayer = Filter.equalTo("Player", data.getPlayer().getName());
    Filter filterServer = Filter.equalTo("Server", data.getServer().getId());

    // Obtain data that fits all the above filters.
    Firestore dataBase = FirestoreClient.getFirestore();
    ApiFuture<QuerySnapshot> documentReference = dataBase.collection("gameScores").
            where(filterServer).
            where(filterGame).
            where(filterDate).
            where(filterPlayer).get();

    // Check if the data is present. If so, it is a duplicate.
    List<QueryDocumentSnapshot> queryDocumentSnapshots = null;
    try {
      queryDocumentSnapshots = documentReference.get().getDocuments();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    // If the filters produce an empty list, the data is not a duplicate.
    if (queryDocumentSnapshots != null && queryDocumentSnapshots.isEmpty()) {
      ret = false;
    }

    return ret;
  }

  public static String getScoreForGameForDay(GameType game, Date date, Guild server) {
    Filter filterGame = Filter.equalTo("Game", game.toString());
    Filter filterDate = Filter.equalTo("Date", date.toString());
    Filter filterServer = Filter.equalTo("Server", server.getId());
    Firestore dataBase = FirestoreClient.getFirestore();
    ApiFuture<QuerySnapshot> documentReference = dataBase.collection("gameScores").
            where(filterServer).where(filterGame).where(filterDate).get();
    List<QueryDocumentSnapshot> queryDocumentSnapshots = null;
    try {
      queryDocumentSnapshots = documentReference.get().getDocuments();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    String data = "Error finding data.";

    if (queryDocumentSnapshots != null) {
      data = "";
      for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
        //String id = qds.getId();
        data += "Player: " + qds.getString("Player") + "\n";
        data += "Game: " + qds.getString("Game") + "\n";
        data += "Score:\n" + qds.getString("Score") + "\n";
        data += "\n";
      }
    }

    return data;
  }

  public static void saveServerSettings(Guild server, MessageChannel channel) throws ExecutionException, InterruptedException {
    Map<String, String> dataSubmission = new HashMap<>();
    dataSubmission.put("Dedicated Channel ID", channel.getId());

    Firestore dataBase = FirestoreClient.getFirestore();
    dataBase.collection("serverSettings").document(server.getId()).set(dataSubmission);
  }

  public static MessageChannel getDedicatedChannel(Guild server, MessageChannel channel) {
    String serverID = server.getId();
    MessageChannel ret = channel;

    Firestore dataBase = FirestoreClient.getFirestore();
    Object channelID = null;
    try {
      channelID = dataBase.collection("serverSettings").document(serverID).get().get().get("Dedicated Channel ID");
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    if (channelID != null) {
      ret = server.getChannelById(MessageChannel.class, channelID.toString());
    }

    return ret;
  }

  public static boolean deleteScore(ScoreData data) {
    boolean dataFound = false;

    // Filters to find that data (score does not matter).
    Filter filterDate = Filter.equalTo("Date", data.getDate().toString());
    Filter filterGame = Filter.equalTo("Game", data.getGame().toString());
    Filter filterPlayer = Filter.equalTo("Player", data.getPlayer().getName());
    Filter filterServer = Filter.equalTo("Server", data.getServer().getId());

    // Obtain data that fits all the above filters.
    Firestore dataBase = FirestoreClient.getFirestore();
    ApiFuture<QuerySnapshot> documentReference = dataBase.collection("gameScores").
            where(filterServer).
            where(filterGame).
            where(filterDate).
            where(filterPlayer).get();

    // Check if the data is present. If so, it is a duplicate.
    List<QueryDocumentSnapshot> queryDocumentSnapshots = null;
    try {
      queryDocumentSnapshots = documentReference.get().getDocuments();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    // If the data is there, delete it. In theory, this should just be a list of size 1.
    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
      dataFound = true;
      for(QueryDocumentSnapshot qds : queryDocumentSnapshots) {
        dataBase.collection("gameScores").document(qds.getId()).delete();
      }
    }

    return dataFound;
  }
}
