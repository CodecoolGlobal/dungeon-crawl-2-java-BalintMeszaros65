package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Ghost extends Actor{

    public Ghost(Cell cell) {
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
