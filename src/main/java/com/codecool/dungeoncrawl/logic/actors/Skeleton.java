package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;

public class Skeleton extends Actor {
    public Skeleton(Cell cell) {
        super(cell);
    }

    @Override
    public boolean validateMove(int dx, int dy) {
        return !isNeighborActor(dx, dy) && !isNeighborItem(dy, dy) && isNeighborCellType(dx, dy, CellType.FLOOR);
    }

    @Override
    public void attack(int x, int y, int damage) {
    }

    @Override
    public String getTileName() {
        return "skeleton";
    }
}
