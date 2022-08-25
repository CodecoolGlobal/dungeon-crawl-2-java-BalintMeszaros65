package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class Tiles {
    public static int TILE_WIDTH = 32;

    private static final Image tileset = new Image("/tiles.png", 543 * 2, 543 * 2, true, false);
    private static final Map<String, Tile> tileMap = new HashMap<>();
    public static class Tile {
        public final int x, y, w, h;
        Tile(int i, int j) {
            x = i * (TILE_WIDTH + 2);
            y = j * (TILE_WIDTH + 2);
            w = TILE_WIDTH;
            h = TILE_WIDTH;
        }
    }

    static {
        tileMap.put("empty", new Tile(0, 0));
        tileMap.put("wall", new Tile(10, 17));
        tileMap.put("floor", new Tile(2, 0));
        tileMap.put("player", new Tile(27, 0));
        tileMap.put("skeleton", new Tile(29, 6));
        tileMap.put("zombie", new Tile(28, 6));
        tileMap.put("ghost", new Tile(26, 6));
        tileMap.put("sword", new Tile(4, 30));
        tileMap.put("coin", new Tile(22, 4));
        tileMap.put("health-potion", new Tile(17, 25));
        tileMap.put("key", new Tile(18, 23));
        tileMap.put("stairs-up", new Tile(2, 6));
        tileMap.put("stairs-down", new Tile(3, 6));
        tileMap.put("open-door", new Tile(2, 9));
        tileMap.put("closed-door", new Tile(0, 9));
        tileMap.put("shield", new Tile(5, 26));
        tileMap.put("coin-chest", new Tile(8, 6));
        tileMap.put("opened-chest", new Tile(9, 6));
    }

    public static void drawTile(GraphicsContext context, Drawable d, int x, int y) {
        Tile tile = tileMap.get(d.getTileName());
        context.drawImage(tileset, tile.x, tile.y, tile.w, tile.h,
                x * TILE_WIDTH, y * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
    }
}
