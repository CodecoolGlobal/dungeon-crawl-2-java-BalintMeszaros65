package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;

public class Zombie extends Actor{


    public Zombie(Cell cell) {
        super(cell);
    }

    @Override
    public boolean validateMove(int dx, int dy) {
        Cell nextCell = super.getCell().getNeighbor(dx, dy);
        return !nextCell.isCellType(CellType.EMPTY) && !nextCell.hasActor() && !nextCell.hasItem();
    }

    @Override
    public boolean attack(int x, int y, int damage) {
        return false;
    }

    @Override
    public String getTileName() {
        return null;
    }
}
