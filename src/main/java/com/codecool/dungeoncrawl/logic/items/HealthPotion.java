package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class HealthPotion extends Item {
    private static int healsAmount = 0;

    public HealthPotion(Cell cell) {
        super(cell);
        healsAmount = 5;
    }

    @Override
    public String getTileName() {
        return "health-potion";
    }

    public static int getHealsAmount() {
        return healsAmount;
    }

}
