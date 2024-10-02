package fr.lernejo;

import fr.lernejo.navy_battle.Launcher;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LauncherTest {
    @Test
    void testServerStart() {
        assertDoesNotThrow(() -> {
            Thread serverThread = new Thread(() -> {
                try {
                    Launcher.main(new String[]{"9876"});
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
            serverThread.start();
            Thread.sleep(2000);
            URL url = new URL("http://localhost:9876/ping");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            assertEquals(200, connection.getResponseCode());
        });
    }
}
