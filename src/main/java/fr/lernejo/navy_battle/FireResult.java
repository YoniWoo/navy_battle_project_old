package fr.lernejo.navy_battle;

public class FireResult {
    private final String consequence;
    private final boolean shipLeft;

    public FireResult(String consequence, boolean shipLeft) {
        this.consequence = consequence;
        this.shipLeft = shipLeft;
    }

    public String getConsequence() {
        return consequence;
    }

    public boolean areShipsLeft() {
        return shipLeft;
    }
}
