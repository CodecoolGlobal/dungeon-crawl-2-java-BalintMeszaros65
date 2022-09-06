package com.codecool.dungeoncrawl.logic;

public enum Direction {
    NORTH (-1, 0),
    SOUTH (1, 0),
    WEST (0, -1),
    EAST (0, 1);

    private final int dRow;
    private final int dCol;

    Direction(int dRow, int dCol) {
        this.dRow = dRow;
        this.dCol = dCol;
    }

    public int getDirectionDRow() {
        return dRow;
    }

    public int getDirectionDCol() {
        return dCol;
    }
}
