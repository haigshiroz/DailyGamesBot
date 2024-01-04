package model.game.data;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import model.game.GameType;

public class FirebaseService {
  public static void saveScore(ScoreData data) throws ExecutionException, InterruptedException {
    Map<String, String> dataSubmission = new HashMap<>();
    dataSubmission.put("Date", data.getDate().toString());
    dataSubmission.put("Game", data.getGame().toString());
    dataSubmission.put("Player", data.getPlayer().getName());
    dataSubmission.put("Score", data.getScore());
    dataSubmission.put("Server", data.getServer().getId());

    Firestore dataBase = FirestoreClient.getFirestore();
    //ApiFuture<WriteResult> apiWriteCallResult =
    dataBase.collection("gameScores").document().set(dataSubmission);
    //return apiWriteCallResult.get().getUpdateTime().toString();
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
}
