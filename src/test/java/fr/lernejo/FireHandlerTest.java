package fr.lernejo;

import com.sun.net.httpserver.HttpServer;
import fr.lernejo.navy_battle.FireHandler;
import fr.lernejo.navy_battle.GameBoard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FireHandlerTest {
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
        GameBoard gameBoard = new GameBoard();
        server.createContext("/api/game/fire", new FireHandler(gameBoard));
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
    void testFireHandler() throws Exception {
        URL url = new URL("http://localhost:" + port + "/api/game/fire?cell=A1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        assertEquals(200, connection.getResponseCode());
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        assertTrue(response.toString().contains("hit"));
    }
}
