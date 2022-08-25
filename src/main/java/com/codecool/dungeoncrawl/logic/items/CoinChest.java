package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class CoinChest extends Item{

    int numberOfCoins;

    public CoinChest(Cell cell) {
        super(cell);
        this.numberOfCoins = 5;
    }

    public int getNumberOfCoins() {
        return numberOfCoins;
    }

    public void lootChest() {
        numberOfCoins = 0;
    }

    @Override
    public String getTileName() {
        if (numberOfCoins > 0) {
            return "coin-chest";
        } else {
            return "opened-chest";
        }
    }
}
