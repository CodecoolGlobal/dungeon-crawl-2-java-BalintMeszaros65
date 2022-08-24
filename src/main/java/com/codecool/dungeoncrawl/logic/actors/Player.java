package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.items.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player extends Actor {

    // TODO inventory
    private Map<String, Integer> inventory;

    public Player(Cell cell) {
        super(cell);
        this.inventory = new HashMap<>();
    }

    @Override
    public boolean validateMove(int dx, int dy) {
        return false;
    }

    public String getTileName() {
        return "player";
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(Item item) {
        // TODO map inv logic
    }
}
