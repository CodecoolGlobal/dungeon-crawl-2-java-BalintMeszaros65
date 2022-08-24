package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;

public class Zombie extends Actor{


    public Zombie(Cell cell, int health, int damage) {
        super(cell, health, damage);
    }

    @Override
    public boolean validateMove(int dx, int dy) {
        Cell nextCell = this.getCell().getNeighbor(dx, dy);
        return !nextCell.isCellType(CellType.WALL) && !nextCell.hasActor() && !nextCell.hasItem();
    }

    @Override
    public String getTileName() {
        return "zombie";
    }
}
