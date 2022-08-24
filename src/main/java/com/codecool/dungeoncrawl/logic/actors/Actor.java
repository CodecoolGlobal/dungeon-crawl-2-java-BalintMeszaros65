package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Drawable;

// TODO public / private methods
public abstract class Actor implements Drawable {
    private Cell cell;
    private int health;
    private final int damage;
    private boolean isAlive;
    private final int distance;

    public Actor(Cell cell, int health, int damage, int distance) {
        this.cell = cell;
        this.distance = distance;
        this.cell.setActor(this);
        this.health = health;
        this.damage = damage;
        this.isAlive = true;
    }

    public abstract boolean validateMove(int dx, int dy);

    public void attack(int dx, int dy) {
        getCellNeighborActor(dx, dy).sufferDamage(damage);
    }

    public Actor getCellNeighborActor(int dx, int dy) {
       return this.getCell().getNeighborActor(dx, dy);
    }

    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        cell.setActor(null);
        nextCell.setActor(this);
        cell = nextCell;
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
        Cell nextCell;
        try {
           nextCell = cell.getNeighbor(dx, dy);
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            return false;
        }
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

    public int getDistance() {
        return distance;
    }

    public void act(int dx, int dy) {
        if (isNeighborActor(dx, dy)) {
            if (getCellNeighborActor(dx, dy) instanceof Player) {
                attack(dx, dy);
            } else {
                if (validateMove(dx, dy)) {
                    move(dx, dy);
                }
            }
        } else {
            if (validateMove(dx, dy)) {
                move(dx, dy);
            }
        }
    }
}
