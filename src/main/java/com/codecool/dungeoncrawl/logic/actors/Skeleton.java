package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.Util;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Direction;

public class Skeleton extends Actor implements DxDyable {
    public Skeleton(Cell cell) {
        super(cell, 6, 2, 1);
    }

    @Override
    public boolean validateMove(int dx, int dy) {
        return !isNeighborActor(dx, dy) && !isNeighborItem(dy, dy) && isNeighborCellType(dx, dy, CellType.FLOOR);
    }

    public int[] getDxDy() {
        Direction randomDirection = Direction.values()[Util.randInt(0, Direction.values().length - 1)];
        int skeletonDistance = getDistance();
        int dx = randomDirection.getDirectionDCol() * skeletonDistance;
        int dy = randomDirection.getDirectionDRow() * skeletonDistance;
        return new int[] {dx, dy};
    }

    @Override
    public String getTileName() {
        return "skeleton";
    }
}
