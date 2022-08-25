package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Direction;

import java.util.HashMap;
import java.util.Map;

// TODO different print if it has shield
// TODO score
public class Player extends Actor {

    // TODO name
    // TODO validation based on name
    private Map<String, Integer> inventory;

    private Direction direction;

    public Player(Cell cell) {
        super(cell, 10000, 1, 1);
        this.direction = Direction.NORTH;
        this.inventory = new HashMap<>();
    }

    @Override
    public boolean validateMove(int dx, int dy) {
        boolean neighborActor = isNeighborActor(dx, dy);
        boolean neighborCellTypeFloorOrDoor = isNeighborCellType(dx, dy, CellType.FLOOR) ||
                isNeighborCellType(dx, dy, CellType.DOOR);
        boolean closedDoor = false;
        try {
            closedDoor = "closed-door".equals(getCellNeighborItem(dx, dy).getTileName());
        } catch (NullPointerException | ArrayIndexOutOfBoundsException ignore) {}
        return !neighborActor && neighborCellTypeFloorOrDoor && !closedDoor;
    }

    @Override
    public void attack(int dx, int dy) {
        this.getCellNeighborActor(dx, dy).sufferDamage(
                this.getDamage() + inventory.getOrDefault("sword", 0));
        retaliation();
    }

    private void retaliation() {
        Actor enemy = null;
        switch (direction) {
            case NORTH:
                enemy = getCellNeighborActor(0, -1);
                break;
            case SOUTH:
                enemy = getCellNeighborActor(0, 1);
                break;
            case WEST:
                enemy = getCellNeighborActor(-1, 0);
                break;
            case EAST:
                enemy = getCellNeighborActor(1, 0);
                break;
        }
        enemy.updateIsAlive();
        if (enemy.isAlive()) {
            sufferDamage(enemy.getDamage());
        }
    }

    public String getTileName() {
        return "player";
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(String string) {
        inventory.merge(string, 1, Integer::sum);
    }

    public void removeInventoryItem(String item) {
        inventory.put(item, inventory.get(item) - 1);
        if (inventory.get(item) == 0) {
            inventory.remove(item);
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
