package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.*;
import com.codecool.dungeoncrawl.logic.items.*;
import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Player;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
    private final int width;
    private final int height;
    private Cell[][] cells;
    private Player player;
    private List<Actor> enemies;

    private GameMap prevMap;
    private GameMap nextMap;

    public GameMap(int width, int height, CellType defaultCellType) {
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(this, x, y, defaultCellType);
            }
        }
        this.enemies = new ArrayList<>();
        this.prevMap = null;
        this.nextMap = null;
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Actor> getEnemies() {
        return enemies;
    }

    public void setEnemy(Actor enemy) {
        this.enemies.add(enemy);
    }


    public GameMap getPrevMap() {
        return prevMap;
    }

    public void setPrevMap(GameMap prevMap) {
        this.prevMap = prevMap;
    }

    public GameMap getNextMap() {
        return nextMap;
    }

    public void setNextMap(GameMap nextMap) {
        this.nextMap = nextMap;
    }

    public String toTxtFormat() {
        StringBuilder map = new StringBuilder();
        map.append(String.valueOf(height) + ' ' + width + '\n');
        for (Cell[] cellRow : cells) {
            for (Cell cell : cellRow) {
                if (cell.getActor() instanceof Skeleton) {
                    map.append('s');
                } else if (cell.getActor() instanceof Player) {
                    map.append('@');
                } else if (cell.getActor() instanceof Zombie) {
                    map.append('z');
                } else if (cell.getActor() instanceof Ghost) {
                    map.append('g');
                } else if (cell.getItem() instanceof Key) {
                    map.append('k');
                } else if (cell.getItem() instanceof ClosedDoor) {
                    map.append('d');
                } else if (cell.getItem() instanceof Coin) {
                    map.append('c');
                } else if (cell.getItem() instanceof HealthPotion) {
                    map.append('p');
                } else if (cell.getItem() instanceof Sword) {
                    map.append('/');
                } else if (cell.getItem() instanceof Shield) {
                    map.append(')');
                } else if (cell.getItem() instanceof CoinChest) {
                    map.append('=');
                } else if (cell.isCellType(CellType.STAIRS_UP)) {
                    map.append('^');
                } else if (cell.isCellType(CellType.STAIRS_DOWN)) {
                    map.append('v');
                } else if (cell.isCellType(CellType.EMPTY)) {
                    map.append(' ');
                } else if (cell.isCellType(CellType.WALL)) {
                    map.append('#');
                } else if (cell.isCellType(CellType.FLOOR)) {
                    map.append('.');
                } else if (cell.isCellType(CellType.DOOR)) {
                    map.append('o');
                }
            }
            map.append('\n');
        }
        return map.toString();
    }
}
