package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Direction;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;
import java.util.Random;
import java.util.Map;

public class Main extends Application {
    static Random random = new Random();
    GameMap map = MapLoader.loadMap();
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    GridPane ui = new GridPane();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));

        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(canvas);
        borderPane.setRight(ui);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);

        primaryStage.setTitle("Platypus Crawl");
        primaryStage.show();
    }


    private void onKeyPressed(KeyEvent keyEvent) {
        Player player = map.getPlayer();
        int playerDistance = player.getDistance();
        switch (keyEvent.getCode()) {
            // TODO move validation (instance of?)
            // TODO pickup item (mouseclick)
            case W:
                if (player.validateMove(0, -playerDistance)) {
                    player.move(0, -playerDistance);
                }
                player.setDirection(Direction.NORTH);
                actWithEnemies();
                refresh();
                break;
            case S:
                if (player.validateMove(0, playerDistance)) {
                    player.move(0, playerDistance);
                }
                player.setDirection(Direction.SOUTH);
                actWithEnemies();
                refresh();
                break;
            case A:
                if (player.validateMove(-playerDistance, 0)) {
                    player.move(-playerDistance, 0);
                }
                player.setDirection(Direction.WEST);
                actWithEnemies();
                refresh();
                break;
            case D:
                if (player.validateMove(playerDistance, 0)) {
                    player.move(playerDistance, 0);
                }
                player.setDirection(Direction.EAST);
                actWithEnemies();
                refresh();
                break;
            case SPACE:
                switch (player.getDirection()) {
                    case NORTH:
                        if (player.isNeighborActor(0, -1)) {
                            player.attack(0, -1);
                        }
                        break;
                    case SOUTH:
                        if (player.isNeighborActor(0, 1)) {
                            player.attack(0, 1);
                        }
                        break;
                    case WEST:
                        if (player.isNeighborActor(-1, 0)) {
                            player.attack(-1, 0);
                        }
                        break;
                    case EAST:
                        if (player.isNeighborActor(1, 0)) {
                            player.attack(1, 0);
                        }
                        break;
                }
                actWithEnemies();
                refresh();
                break;
        }
    }

    private void actWithEnemies() {
        // TODO enemy checking neighbors for player + if true damage
        // TODO enemy movement method
        List<Actor> enemies = map.getEnemies();
        if (enemies != null) {
            for (Actor enemy : enemies) {
                if (enemy instanceof Skeleton) {
                    actWithSkeleton(enemy);
                } else if (enemy instanceof Ghost) {
                    actWithGhost(enemy);
                } else if (enemy instanceof Zombie) {
                    actWithZombie(enemy);
                }
            }
        }
    }

    private void actWithSkeleton(Actor skeleton) {
        int dx = 0;
        int dy = 0;
        int skeletonDistance = skeleton.getDistance();
        if (randInt(0, 1) == 0) {
            if (randInt(0, 1) == 0) {
                dx = skeletonDistance;
            } else {
                dx = -skeletonDistance;
            }
        } else {
            if (randInt(0, 1) == 0) {
                dy = skeletonDistance;
            } else {
                dy = -skeletonDistance;
            }
        }
        actWithEnemy(skeleton, dx, dy);

    }

    private void actWithGhost(Actor ghost) {
        int ghostDistance = ghost.getDistance();
        int dx = randInt(-ghostDistance, ghostDistance);
        int dy = randInt(-ghostDistance, ghostDistance);
        actWithEnemy(ghost, dx, dy);
    }

    private void actWithZombie(Actor zombie) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) {
                    actWithEnemy(zombie, - i, - j);
                }
            }
        }
    }

    private void actWithEnemy(Actor enemy, int dx, int dy) {
        if (enemy.isNeighborActor(dx, dy)) {
            if (enemy.getCellNeighborActor(dx, dy) instanceof Player) {
                enemy.attack(dx, dy);
            } else {
                if (enemy.validateMove(dx, dy)) {
                    enemy.move(dx, dy);
                }
            }
        } else {
            if (enemy.validateMove(dx, dy)) {
                enemy.move(dx, dy);
            }
        }
    }

    private void refresh() {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x, y);
                } else if (cell.getItem() != null) {
                    Tiles.drawTile(context, cell.getItem(), x, y);
                } else {
                    Tiles.drawTile(context, cell, x, y);
                }
            }
        }
        ui.add(new Label("Health:"), 0, 0);
        ui.add(new Label(String.format("%30s", map.getPlayer().getHealth())), 0, 0);
        Map<String, Integer> inventory = map.getPlayer().getInventory();
        inventory.put("key1", 1);
        inventory.put("key2", 2);
        inventory.put("key3", 3);
        int positionOnUI = 1;
        for (String key : inventory.keySet()) {
            String itemCount = String.format("%30s", inventory.get(key));
            ui.add(new Label(key.substring(0,1).toUpperCase() + key.substring(1) + ":"), 0, positionOnUI);
            ui.add(new Label(itemCount), 0, positionOnUI);
            positionOnUI++;
        }
    }

    public static int randInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }
}
