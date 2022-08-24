package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Drawable;

public abstract class Actor implements Drawable {
    private Cell cell;
    private int health;
    private final int damage;
    private boolean isAlive;

    public Actor(Cell cell, int health, int damage) {
        this.cell = cell;
        this.cell.setActor(this);
        this.health = health;
        this.damage = damage;
        this.isAlive = true;
    }

    // TODO validate in subclasses
    public abstract boolean validateMove(int dx, int dy);

    public void attack(int dx, int dy) {
        this.getCell().getNeighbor(dx, dy).getActor().sufferDamage(damage);
    }

    public void move(int dx, int dy) {
        if (validateMove(dx, dy)) {
            Cell nextCell = cell.getNeighbor(dx, dy);
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;
        }
    }

    public void sufferDamage (int damage) {
        this.health -= damage;
    }

    public int getHealth() {
        return health;
    }

    public Cell getCell() {
        return cell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }

    public boolean isNeighborActor (int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        return nextCell.hasActor();
    }

    public boolean isNeighborItem (int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        return nextCell.hasItem();
    }

    public boolean isNeighborCellType (int dx, int dy, CellType cellType) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        return nextCell.isCellType(cellType);
    }

    public int getDamage() {
        return damage;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void updateIsAlive() {
        if (health <= 0) {
            isAlive = false;
        }
    }
}
