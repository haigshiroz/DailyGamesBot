package discordbot.data;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class to contain the method to initialize the Firebase database.
 */
public class FirebaseInitializer {
  /**
   * Initializes the Firebase database.
   */
  public static void initialize() {
    try {
      InputStream serviceAccount =
              new FileInputStream("./src/main/resources/firebaseKey.json");

      FirebaseOptions options = FirebaseOptions.builder()
              .setCredentials(GoogleCredentials.fromStream(serviceAccount))
              .setDatabaseUrl("https://dailygamesbot.firebaseio.com/")
              .build();

      FirebaseApp.initializeApp(options);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
