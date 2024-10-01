package fr.lernejo.navy_battle;

import java.util.HashSet;
import java.util.Set;

public class Ship {
    private final Set<String> positions;
    private final Set<String> hits;

    public Ship(Set<String> positions) {
        this.positions = new HashSet<>(positions);
        this.hits = new HashSet<>();
    }

    public boolean hit(String cell) {
        if (positions.contains(cell)) {
            hits.add(cell);
            return true;
        }
        return false;
    }

    public boolean isSunk() {
        return hits.containsAll(positions);
    }

    public boolean contains(String cell) {
        return positions.contains(cell);
    }
}
