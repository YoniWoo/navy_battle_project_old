package fr.lernejo.navy_battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class GameBoard {
    private final List<Ship> ships;
    private final List<String> firedCells;

    public GameBoard() {
        this.ships = new ArrayList<>();
        this.firedCells = new ArrayList<>();
        initializeShips();
    }

    private void initializeShips() {
        ships.add(new Ship(Set.of("A1", "A2", "A3", "A4", "A5")));
        ships.add(new Ship(Set.of("B1", "B2", "B3", "B4")));
        ships.add(new Ship(Set.of("C1", "C2", "C3")));
        ships.add(new Ship(Set.of("D1", "D2")));
    }

    public FireResult fireAt(String cell) {
        if (!firedCells.contains(cell)) {
            Optional<Ship> shipOpt = findShipAt(cell);
            if (shipOpt.isPresent()) {
                Ship ship = shipOpt.get();
                boolean isSunk = ship.isSunk();
                return new FireResult(isSunk ? "sunk" : "hit", areShipsLeft());
            } else {
                return new FireResult("miss", areShipsLeft());
            }
        }

        firedCells.add(cell);

        Optional<Ship> shipOpt = findShipAt(cell);
        if (shipOpt.isPresent()) {
            Ship ship = shipOpt.get();
            ship.hit(cell);
            boolean isSunk = ship.isSunk();
            return new FireResult(isSunk ? "sunk" : "hit", areShipsLeft());
        } else {
            return new FireResult("miss", areShipsLeft());
        }
    }

    private Optional<Ship> findShipAt(String cell) {
        return ships.stream().filter(ship -> ship.contains(cell)).findFirst();
    }

    public boolean areShipsLeft() {
        return ships.stream().anyMatch(ship -> !ship.isSunk());
    }
}
