package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class HealthPotion extends Item {

    public HealthPotion(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "health-potion";
    }

    @Override
    public String toString() {
        return "Health potion";
    }
}
