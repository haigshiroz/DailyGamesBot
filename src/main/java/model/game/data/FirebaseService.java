package model.game.data;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

import net.dv8tion.jda.api.entities.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FirebaseService {
  public String saveScore(String score, User player, String game, Date dayOfGame) throws ExecutionException, InterruptedException {
    Firestore dataBase = FirestoreClient.getFirestore();
    Map<String, String> dataSubmission = new HashMap<>();
    dataSubmission.put("Date", dayOfGame.toString());
    dataSubmission.put("Game", game);
    dataSubmission.put("Player", player.getName());
    dataSubmission.put("Score:", score);

    ApiFuture<WriteResult> apiWriteCallResult = dataBase.collection("gameScores").document().set(dataSubmission);

    return apiWriteCallResult.get().getUpdateTime().toString();
  }
}
