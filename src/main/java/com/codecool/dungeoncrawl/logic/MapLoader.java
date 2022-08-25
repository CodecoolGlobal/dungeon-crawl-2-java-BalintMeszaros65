package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Ghost;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.actors.Zombie;
import com.codecool.dungeoncrawl.logic.items.*;

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
                        case ' ':
                            cell.setType(CellType.EMPTY);
                            break;
                        case '#':
                            cell.setType(CellType.WALL);
                            break;
                        case '.':
                            cell.setType(CellType.FLOOR);
                            break;
                        case 's':
                            cell.setType(CellType.FLOOR);
                            map.setEnemy(new Skeleton(cell));
                            break;
                        case '@':
                            cell.setType(CellType.FLOOR);
                            map.setPlayer(new Player(cell));
                            break;
                        case 'z':
                            cell.setType(CellType.FLOOR);
                            map.setEnemy(new Zombie(cell));
                            break;
                        case 'g':
                            cell.setType(CellType.FLOOR);
                            map.setEnemy(new Ghost(cell));
                            break;
                        case 'k':
                            cell.setType(CellType.FLOOR);
                            map.setItem(new Key(cell));
                            break;
                        case 'd':
                            cell.setType(CellType.DOOR);
                            map.setItem(new ClosedDoor(cell));
                            break;
                        case 'c':
                            cell.setType(CellType.FLOOR);
                            map.setItem(new Coin(cell));
                            break;
                        case 'p':
                            cell.setType(CellType.FLOOR);
                            map.setItem(new HealthPotion(cell));
                            break;
                        case '/':
                            cell.setType(CellType.FLOOR);
                            map.setItem(new Sword(cell));
                            break;
                         case '^':
                            cell.setType(CellType.STAIRSUP);
                            break;
                        case 'v':
                            cell.setType(CellType.STAIRSDOWN);
                            break;
                        case ')':
                            cell.setType(CellType.FLOOR);
                            map.setItem(new Shield(cell));
                            break;
                        default:
                            throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }
        }
        return map;
    }

}
