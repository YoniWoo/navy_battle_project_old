package fr.lernejo;

import com.sun.net.httpserver.HttpServer;
import fr.lernejo.navy_battle.GameInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameStartHandlerTest {
    private HttpServer server;
    private int port;

    private int findPort() throws Exception {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        port = findPort();
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/game/start", new GameInitializer());
        server.setExecutor(null);
        server.start();
    }
    @AfterEach
    void tearDown() throws Exception {
        if (server != null) {
            server.stop(0);
        }
    }
    @Test
    void testGameStart() throws Exception {
        URL url = new URL("http://localhost:" + port + "/api/game/start");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        String jsonInputString = "{\"id\":\"1\", \"url\":\"http://localhost:9876\", \"message\":\"hello\"}";
        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input);
        }

        assertEquals(202, connection.getResponseCode());
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        assertTrue(response.toString().contains("http://localhost:" + port));
        assertTrue(response.toString().contains("May the best code win"));
    }
}
