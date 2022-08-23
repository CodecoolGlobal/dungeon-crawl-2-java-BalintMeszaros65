package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.items.Item;

import java.util.List;
import java.util.Map;

public class Player extends Actor {

    // TODO inventory
    private Map<Item, Integer> inventory;

    public Player(Cell cell) {
        super(cell);
    }

    @Override
    public boolean validateMove(int dx, int dy) {
        return false;
    }

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
