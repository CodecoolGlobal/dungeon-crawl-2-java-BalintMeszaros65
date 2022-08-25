package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Direction;

import java.util.HashMap;
import java.util.Map;


// TODO score
public class Player extends Actor {
    private Map<String, Integer> inventory;
    private boolean noClip;
    private Direction direction;

    public Player(Cell cell) {
        super(cell, 100, 1, 1);
        this.direction = Direction.NORTH;
        this.inventory = new HashMap<>();
        this.noClip = false;
    }

    @Override
    public boolean validateMove(int dx, int dy) {
        boolean neighborActor = isNeighborActor(dx, dy);
        boolean neighborCellTypeFloorOrDoor = isNeighborCellType(dx, dy, CellType.FLOOR) ||
                isNeighborCellType(dx, dy, CellType.DOOR) || isNeighborCellType(dx, dy, CellType.STAIRSDOWN) ||
                isNeighborCellType(dx, dy, CellType.STAIRSUP);
        boolean closedDoor = false;
        try {
            closedDoor = "closed-door".equals(getCellNeighborItem(dx, dy).getTileName());
        } catch (NullPointerException | ArrayIndexOutOfBoundsException ignore) {}
        if (noClip) {
            return true;
        } else {
            return !neighborActor && neighborCellTypeFloorOrDoor && !closedDoor;
        }
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

    @Override
    public void sufferDamage(int damage) {
        if (inventory.containsKey("shield")) {
            setHealth(getHealth() - damage / 2);
            removeInventoryItem("shield");
        } else {
            setHealth(getHealth() - damage);
        }
    }

    public String getTileName() {
        if (inventory.containsKey("sword") && inventory.containsKey("shield")) {
            return "player";
        } else if (inventory.containsKey("sword")) {
            return "player-with-sword";
        } else if (inventory.containsKey("shield")) {
            return "player-with-shield";
        } else {
            return "player-naked";
        }
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(Map<String, Integer> inventory) {
        this.inventory = inventory;
    }

    public void putItemToInventory(String string) {
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

    public void setCheater() {
        this.noClip = true;
        setHealth(10000);
    }

    public boolean getCheater() {
        return noClip;
    }
}
