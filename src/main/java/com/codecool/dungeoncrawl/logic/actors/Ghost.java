package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;

import static com.codecool.dungeoncrawl.Main.randInt;

public class Ghost extends Actor implements DxDyable {

    public Ghost(Cell cell) {
        super(cell, 5, 1, 2);
    }

    @Override
    public boolean validateMove(int dx, int dy) {
        Cell nextCell;
        try {
            nextCell = this.getCell().getNeighbor(dx, dy);
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            return false;
        }
        return !nextCell.isCellType(CellType.EMPTY) && !nextCell.hasActor() && !nextCell.hasItem();
    }

    public int[] getDxDy() {
        int ghostDistance = getDistance();
        int dx = randInt(-ghostDistance, ghostDistance);
        int dy = randInt(-ghostDistance, ghostDistance);
        return new int[] {dx, dy};
    }

    @Override
    public String getTileName() {
        return "ghost";
    }
}
