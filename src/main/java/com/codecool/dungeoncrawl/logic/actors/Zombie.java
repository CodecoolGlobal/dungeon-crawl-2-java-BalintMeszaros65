package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Zombie extends Actor{


    public Zombie(Cell cell) {
        super(cell, 2, 4, 0);
    }

    @Override
    public boolean validateMove(int dx, int dy) {
        return false; // bc zombies doesn't move;
    }

    @Override
    public String getTileName() {
        return "zombie";
    }
}
