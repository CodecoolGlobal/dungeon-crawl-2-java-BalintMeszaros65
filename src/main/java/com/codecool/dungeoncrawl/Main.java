package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Direction;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.*;
import com.codecool.dungeoncrawl.logic.items.ClosedDoor;
import com.codecool.dungeoncrawl.logic.items.HealthPotion;
import com.codecool.dungeoncrawl.logic.items.Item;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

// TODO sounds
// TODO map
// TODO zoom
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
        if (player.isAlive()) {
            switch (keyEvent.getCode()) {
                case Q:
                    if (player.getInventory().containsKey("health-potion")) {
                        player.healUp(HealthPotion.getHealsAmount());
                        player.removeInventoryItem("health-potion");
                    }
                    actWithEnemies();
                    refresh();
                    break;
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
            player.updateIsAlive();
        }
    }



    private void actWithEnemies() {
        List<Actor> enemies = map.getEnemies();
        List<Actor> enemiesToBeRemoved = new ArrayList<>();
        if (enemies != null) {
            for (Actor enemy : enemies) {
                if (enemy.isAlive()) {
                    if (enemy instanceof Zombie) {
                        enemy.act(0, 0);
                    } else if (enemy instanceof Skeleton) {
                        Skeleton skeleton = (Skeleton) enemy;
                        int[] DxDy = skeleton.getDxDy();
                        skeleton.act(DxDy[0], DxDy[1]);
                    } else if (enemy instanceof Ghost) {
                        Ghost ghost = (Ghost) enemy;
                        int[] DxDy = ghost.getDxDy();
                        ghost.act(DxDy[0], DxDy[1]);
                    }
                    enemy.updateIsAlive();
                } else {
                    enemy.getCell().setActor(null);
                    enemiesToBeRemoved.add(enemy);
                }
            }
            for (Actor enemyToBeRemoved: enemiesToBeRemoved) {
                enemies.remove(enemyToBeRemoved);
            }
        }
    }


    private void refresh() {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.hasActor()) {
                    Tiles.drawTile(context, cell.getActor(), x, y);
                } else if (cell.hasItem()) {
                    Tiles.drawTile(context, cell.getItem(), x, y);
                } else {
                    Tiles.drawTile(context, cell, x, y);
                }
            }
        }
        fillGridPane();
    }

    private void fillGridPane() {
        Player player = map.getPlayer();
        ui.getChildren().clear();
        ui.add(new Label("Health:"), 0, 0);
        ui.add(new Label(String.format("%30s", player.getHealth())), 0, 0);
        Map<String, Integer> inventory = player.getInventory();
        int positionOnUI = 1;
        for (String key : inventory.keySet()) {
            String itemCount = String.format("%30s", inventory.get(key));
            ui.add(new Label(key.substring(0,1).toUpperCase() + key.substring(1) + ":"), 0, positionOnUI);
            ui.add(new Label(itemCount), 0, positionOnUI);
            positionOnUI++;
        }
        addItemButton(positionOnUI);
        addDoorButton(positionOnUI + 1);

    }

    private void addItemButton(int positionOnUI) {
        Player player = map.getPlayer();
        if (player.getCell().hasItem()) {
            Item item = player.getCell().getItem();
            Button itemButton = new Button("Pick up " + item.getTileName());
            ui.add(itemButton, 0, positionOnUI);
            itemButton.setOnAction(event -> {
                player.setInventory(item.getTileName());
                player.getCell().setItem(null);
                refresh();
            });
        }
    }

    private void addDoorButton(int positionOnUI) {
        Player player = map.getPlayer();
        int[] closedDoorPosition = new int[0];
        try {
            if (player.getCell().getNeighborItem(0, 1) instanceof ClosedDoor) {
                closedDoorPosition = new int[]{0, 1};
            } else if (player.getCell().getNeighborItem(0, -1) instanceof ClosedDoor) {
                closedDoorPosition = new int[]{0, -1};
            } else if (player.getCell().getNeighborItem(1, 0) instanceof ClosedDoor) {
                closedDoorPosition = new int[]{1, 0};
            } else if (player.getCell().getNeighborItem(-1, 0) instanceof ClosedDoor) {
                closedDoorPosition = new int[]{-1, 0};
            }
        } catch (IndexOutOfBoundsException ignore) {}
        if (closedDoorPosition.length == 2 && player.getInventory().containsKey("key")) {
            Button doorButton = new Button("Open door!");
            ui.add(doorButton, 0, positionOnUI);
            int[] finalClosedDoorPosition = closedDoorPosition;
            doorButton.setOnAction(event -> {
                player.removeInventoryItem("key");
                map.getCell(player.getX() + finalClosedDoorPosition[0],
                        player.getY() + finalClosedDoorPosition[1]).setItem(null);
                refresh();
            });
        }
    }

    public static int randInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }
}
