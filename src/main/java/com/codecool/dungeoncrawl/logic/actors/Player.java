package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Sword;

import java.util.List;
import java.util.Map;

public class Player extends Actor {

    // TODO inventory
    private Map<String, Integer> inventory;

    public Player(Cell cell) {
        super(cell);
    }

    @Override
    public boolean validateMove(int dx, int dy) {
        return !isNeighborActor(dx, dy) && isNeighborCellType(dx, dy, CellType.FLOOR);
    }

    @Override
    public void attack(int dx, int dy) {
        this.getCell().getNeighbor(dx, dy).getActor().sufferDamage(
                this.getDamage() + inventory.getOrDefault("sword", 0));
    };

    public String getTileName() {
        return "player";
    }

    public Map<Item, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(Item item) {
        // TODO map inv logic
    }
}
