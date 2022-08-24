package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Zombie extends Actor{


    public Zombie(Cell cell, int health, int damage) {
        super(cell, health, damage);
    }

    @Override
    public boolean validateMove(int dx, int dy) {
        return true; // bc zombies doesn't move;
    }

    @Override
    public String getTileName() {
        return "zombie";
    }
}
