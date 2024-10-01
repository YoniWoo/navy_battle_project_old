package fr.lernejo.navy_battle;

public class GameStartResponse {
    public String id;
    public String url;
    public String message;

    public GameStartResponse(String id, String url, String message) {
        this.id = id;
        this.url = url;
        this.message = message;
    }
}
