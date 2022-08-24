package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;

public class Ghost extends Actor{

    public Ghost(Cell cell) {
        super(cell);

    }

    @Override
    public boolean validateMove(int dx, int dy) {
        Cell nextCell = super.getCell().getNeighbor(dx, dy);
        return !nextCell.isCellType(CellType.EMPTY) && !nextCell.hasActor() && !nextCell.hasItem();
    }

    @Override
    public void attack(int x, int y, int damage) {

    }

    @Override
    public String getTileName() {
        return "ghost";
    }
}
