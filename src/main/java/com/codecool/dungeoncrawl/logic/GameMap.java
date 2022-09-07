package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameMap {
    private final int width;
    private final int height;
    private Cell[][] cells;

    private Player player;

    private List<Actor> enemies;

    private List<Item> items;

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
        this.items = new ArrayList<>();
    }

    public GameMap changeMap(GameMap nextMap) {
        int prevHealth;
        boolean prevCheater;
        Map<String, Integer> prevInventory;
        prevHealth = this.getPlayer().getHealth();
        prevInventory = this.getPlayer().getInventory();
        prevCheater = this.getPlayer().getCheater();
        nextMap.getPlayer().setHealth(prevHealth);
        nextMap.getPlayer().setInventory(prevInventory);
        if (prevCheater) {
            nextMap.getPlayer().setCheater();
        }
        return nextMap;
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

    public void setItem(Item item) {
        this.items.add(item);
    }
}
