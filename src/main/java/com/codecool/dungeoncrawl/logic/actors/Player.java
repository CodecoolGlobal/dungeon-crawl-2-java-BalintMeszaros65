package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Direction;

import java.util.HashMap;
import java.util.Map;

public class Player extends Actor {

    // TODO inventory
    private Map<String, Integer> inventory;

    private Direction direction;

    public Player(Cell cell) {
        super(cell, 10, 1, 1);
        this.direction = Direction.NORTH;
        this.inventory = new HashMap<>();
    }

    @Override
    public boolean validateMove(int dx, int dy) {
        return !isNeighborActor(dx, dy) && isNeighborCellType(dx, dy, CellType.FLOOR);
    }

    // TODO monster retaliation
    @Override
    public void attack(int dx, int dy) {
        this.getCellNeighborActor(dx, dy).sufferDamage(
                this.getDamage() + inventory.getOrDefault("sword", 0));
    };

    public String getTileName() {
        return "player";
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(String string) {
        // TODO map inv logic
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
