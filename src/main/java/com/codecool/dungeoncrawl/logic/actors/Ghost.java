package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.Util;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;

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
        if (nextCell != null) {
            return !nextCell.isCellType(CellType.EMPTY) && !nextCell.hasActor() && !nextCell.hasItem();
        } else {
            return false;
        }
    }

    public int[] getDxDy() {
        int ghostDistance = getDistance();
        int dx = Util.randInt(-ghostDistance, ghostDistance);
        int dy = Util.randInt(-ghostDistance, ghostDistance);
        return new int[] {dx, dy};
    }

    @Override
    public String getTileName() {
        return "ghost";
    }
}
