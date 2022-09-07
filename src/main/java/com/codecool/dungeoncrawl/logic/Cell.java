package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.items.Item;

public class Cell implements Drawable {
    private CellType type;
    private Actor actor;
    private Item item;
    private GameMap gameMap;
    private int x, y;

    Cell(GameMap gameMap, int x, int y, CellType type) {
        this.gameMap = gameMap;
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Actor getActor() {
        return actor;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Cell getNeighbor (int dx, int dy) {
        Cell result = null;
        try {
            result = gameMap.getCell(x + dx, y + dy);
        } catch (IndexOutOfBoundsException ignore) {}
        return result;
    }

    public Actor getNeighborActor (int dx, int dy) {
        return gameMap.getCell(x + dx, y + dy).getActor();
    }

    public Item getNeighborItem (int dx, int dy) {
        return gameMap.getCell(x + dx, y + dy).getItem();
    }

    @Override
    public String getTileName() {
        return type.getTileName();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean hasActor() {
        return actor != null;
    }

    public boolean hasItem() {
        return item != null;
    }

    public boolean isCellType(CellType cellType) {
        return type == cellType;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

}
