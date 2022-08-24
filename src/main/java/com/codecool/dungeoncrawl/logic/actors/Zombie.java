package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Zombie extends Actor{


    public Zombie(Cell cell) {
        super(cell);
    }

    @Override
    public boolean validateMove(int dx, int dy) {
        return false;
    }

    @Override
    public String getTileName() {
        return null;
    }
}
