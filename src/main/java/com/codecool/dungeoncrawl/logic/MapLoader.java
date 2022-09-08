package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Ghost;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.actors.Zombie;
import com.codecool.dungeoncrawl.logic.items.*;
import com.codecool.dungeoncrawl.model.GameState;

import java.io.InputStream;
import java.util.Scanner;

public class MapLoader {
    public static GameMap loadMap(String mapName) {
        InputStream is = MapLoader.class.getResourceAsStream("/" + mapName + ".txt");
        Scanner scanner = new Scanner(is);
        int width = scanner.nextInt();
        int height = scanner.nextInt();

        scanner.nextLine(); // empty line

        GameMap map = new GameMap(width, height, CellType.EMPTY);
        for (int y = 0; y < height; y++) {
            String line = scanner.nextLine();
            for (int x = 0; x < width; x++) {
                if (x < line.length()) {
                    Cell cell = map.getCell(x, y);
                    switch (line.charAt(x)) {
                        case ' ' -> cell.setType(CellType.EMPTY);
                        case '#' -> cell.setType(CellType.WALL);
                        case '.' -> cell.setType(CellType.FLOOR);
                        case 's' -> {
                            cell.setType(CellType.FLOOR);
                            map.setEnemy(new Skeleton(cell));
                        }
                        case '@' -> {
                            cell.setType(CellType.FLOOR);
                            map.setPlayer(new Player(cell));
                        }
                        case 'z' -> {
                            cell.setType(CellType.FLOOR);
                            map.setEnemy(new Zombie(cell));
                        }
                        case 'g' -> {
                            cell.setType(CellType.FLOOR);
                            map.setEnemy(new Ghost(cell));
                        }
                        case 'k' -> {
                            cell.setType(CellType.FLOOR);
                            new Key(cell);
                        }
                        case 'd' -> {
                            cell.setType(CellType.DOOR);
                            new ClosedDoor(cell);
                        }
                        case 'o' -> cell.setType(CellType.DOOR);
                        case 'c' -> {
                            cell.setType(CellType.FLOOR);
                            new Coin(cell);
                        }
                        case 'p' -> {
                            cell.setType(CellType.FLOOR);
                            new HealthPotion(cell);
                        }
                        case '/' -> {
                            cell.setType(CellType.FLOOR);
                            new Sword(cell);
                        }
                        case '^' -> cell.setType(CellType.STAIRS_UP);
                        case 'v' -> cell.setType(CellType.STAIRS_DOWN);
                        case ')' -> {
                            cell.setType(CellType.FLOOR);
                            new Shield(cell);
                        }
                        case '=' -> {
                            cell.setType(CellType.FLOOR);
                            new CoinChest(cell);
                        }
                        default -> throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }
        }
        return map;
    }

    public static GameMap loadMap(GameState gameState, int mapNumber) {
        String map = null;
        if (mapNumber == 1) {
            map = gameState.getMap1();
        } else if (mapNumber == 2) {
            map = gameState.getMap2();
        } else if (mapNumber == 3) {
            map = gameState.getMap3();
        }
        assert map != null;
        Scanner scanner = new Scanner(map);

        int width = scanner.nextInt();
        int height = scanner.nextInt();

        scanner.nextLine(); // empty line

        GameMap gameMap = new GameMap(width, height, CellType.EMPTY);
        for (int y = 0; y < height; y++) {
            String line = scanner.nextLine();
            for (int x = 0; x < width; x++) {
                if (x < line.length()) {
                    Cell cell = gameMap.getCell(x, y);
                    switch (line.charAt(x)) {
                        case ' ' -> cell.setType(CellType.EMPTY);
                        case '#' -> cell.setType(CellType.WALL);
                        case '.' -> cell.setType(CellType.FLOOR);
                        case 's' -> {
                            cell.setType(CellType.FLOOR);
                            gameMap.setEnemy(new Skeleton(cell));
                        }
                        case '@' -> {
                            cell.setType(CellType.FLOOR);
                            gameMap.setPlayer(new Player(cell));
                        }
                        case 'z' -> {
                            cell.setType(CellType.FLOOR);
                            gameMap.setEnemy(new Zombie(cell));
                        }
                        case 'g' -> {
                            cell.setType(CellType.FLOOR);
                            gameMap.setEnemy(new Ghost(cell));
                        }
                        case 'k' -> {
                            cell.setType(CellType.FLOOR);
                            new Key(cell);
                        }
                        case 'd' -> {
                            cell.setType(CellType.DOOR);
                            new ClosedDoor(cell);
                        }
                        case 'o' -> cell.setType(CellType.DOOR);
                        case 'c' -> {
                            cell.setType(CellType.FLOOR);
                            new Coin(cell);
                        }
                        case 'p' -> {
                            cell.setType(CellType.FLOOR);
                            new HealthPotion(cell);
                        }
                        case '/' -> {
                            cell.setType(CellType.FLOOR);
                            new Sword(cell);
                        }
                        case '^' -> cell.setType(CellType.STAIRS_UP);
                        case 'v' -> cell.setType(CellType.STAIRS_DOWN);
                        case ')' -> {
                            cell.setType(CellType.FLOOR);
                            new Shield(cell);
                        }
                        case '=' -> {
                            cell.setType(CellType.FLOOR);
                            new CoinChest(cell);
                        }
                        default -> throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }
        }
        return gameMap;
    }
}
