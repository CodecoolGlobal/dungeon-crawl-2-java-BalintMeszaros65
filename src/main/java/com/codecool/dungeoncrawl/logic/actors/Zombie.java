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
    public void act(int dx, int dy) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) {
                    if (isNeighborActor(i, j)) {
                        if (getCellNeighborActor(i, j) instanceof Player) {
                            attack(i, j);
                        } else {
                            if (validateMove(i, j)) {
                                move(i, j);
                            }
                        }
                    } else {
                        if (validateMove(i, j)) {
                            move(i, j);
                        }
                    }
                }
            }
        }
    }

    @Override
    public String getTileName() {
        return "zombie";
    }
}
