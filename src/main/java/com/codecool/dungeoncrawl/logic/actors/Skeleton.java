package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.Util;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;

public class Skeleton extends Actor implements DxDyable {
    public Skeleton(Cell cell) {
        super(cell, 6, 2, 1);
    }

    @Override
    public boolean validateMove(int dx, int dy) {
        return !isNeighborActor(dx, dy) && !isNeighborItem(dy, dy) && isNeighborCellType(dx, dy, CellType.FLOOR);
    }

    public int[] getDxDy() {
        int dx = 0;
        int dy = 0;
        int skeletonDistance = getDistance();
        if (Util.randInt(0, 1) == 0) {
            if (Util.randInt(0, 1) == 0) {
                dx = skeletonDistance;
            } else {
                dx = -skeletonDistance;
            }
        } else {
            if (Util.randInt(0, 1) == 0) {
                dy = skeletonDistance;
            } else {
                dy = -skeletonDistance;
            }
        }
        return new int[] {dx, dy};
    }

    @Override
    public String getTileName() {
        return "skeleton";
    }
}
