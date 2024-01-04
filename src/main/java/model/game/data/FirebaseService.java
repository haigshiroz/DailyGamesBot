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

import net.dv8tion.jda.api.entities.User;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import model.game.GameType;

public class FirebaseService {
  public String saveScore(String score, User player, GameType game, Date dayOfGame) throws ExecutionException, InterruptedException {
    Map<String, String> dataSubmission = new HashMap<>();
    dataSubmission.put("Date", dayOfGame.toString());
    dataSubmission.put("Game", game.toString());
    dataSubmission.put("Player", player.getName());
    dataSubmission.put("Score", score);

    Firestore dataBase = FirestoreClient.getFirestore();
    ApiFuture<WriteResult> apiWriteCallResult = dataBase.collection("gameScores").document().set(dataSubmission);

    return apiWriteCallResult.get().getUpdateTime().toString();
  }

  public String getScoreForGameForDay(GameType game, Date date) {
    Filter filterGame = Filter.equalTo("Game", game.toString());
    Filter filterDate = Filter.equalTo("Date", date.toString());

    Firestore dataBase = FirestoreClient.getFirestore();
    ApiFuture<QuerySnapshot> documentReference = dataBase.collection("gameScores").where(filterGame).where(filterDate).get();
    List<QueryDocumentSnapshot> queryDocumentSnapshots = null;
    try {
      queryDocumentSnapshots = documentReference.get().getDocuments();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    String data = "Error finding data.";

    if(queryDocumentSnapshots != null) {
      data = "";
      for(QueryDocumentSnapshot qds : queryDocumentSnapshots) {
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
