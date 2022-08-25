package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.*;
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
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

// TODO sounds
// TODO zoom
public class Main extends Application {
    GameMap map1 = MapLoader.loadMap("map1");
    GameMap map2 = MapLoader.loadMap("map2");
    GameMap map3 = MapLoader.loadMap("map3");
    GameMap map = map1;
    String playerName = setUpPlayerName();
    static Random random = new Random();
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    GridPane ui = new GridPane();
    BorderPane borderPane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));

        borderPane = new BorderPane();

        borderPane.setCenter(canvas);
        borderPane.setRight(ui);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        changeMap();
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
                case ESCAPE:
                    // TODO exit from game and/or program
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
            changeMap();
        } else {
            Canvas canvas = new Canvas(this.canvas.getWidth(), this.canvas.getHeight());
            this.borderPane.setCenter(canvas);
            this.borderPane.setRight(null);

            GraphicsContext context = canvas.getGraphicsContext2D();
            context.setFont(new Font("arial", 42));
            context.setFill(Color.INDIANRED);
            context.setTextAlign(TextAlignment.CENTER);
            context.fillText("You died!", canvas.getWidth() / 2, canvas.getHeight() / 2);
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
            for (Actor enemyToBeRemoved : enemiesToBeRemoved) {
                enemies.remove(enemyToBeRemoved);
            }
        }
    }


    private void refresh() {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                renderArea(x,y);
            }
        }
        fillGridPane();
    }

    private void renderArea(int x, int y) {
        Cell cell = map.getCell(x, y);
        if (cell.hasActor()) {
            Tiles.drawTile(context, cell.getActor(), x, y);
        } else if (cell.hasItem()) {
            Tiles.drawTile(context, cell.getItem(), x, y);
        } else {
            Tiles.drawTile(context, cell, x, y);
        }
    }

    private void fillGridPane() {
        Player player = map.getPlayer();
        ui.getChildren().clear();
        ui.add(new Label("Player: " + playerName), 0, 0);
        ui.add(new Label("Health:"), 0, 1);
        ui.add(new Label(String.format("%30s", player.getHealth())), 0, 1);
        Map<String, Integer> inventory = player.getInventory();
        int positionOnUI = 2;
        for (String key : inventory.keySet()) {
            String itemCount = String.format("%30s", inventory.get(key));
            ui.add(new Label(key.substring(0, 1).toUpperCase() + key.substring(1) + ":"), 0, positionOnUI);
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
                player.putItemToInventory(item.getTileName());
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

    private void changeMap() {
        if (map.getPlayer().isNeighborCellType(0, 0, CellType.STAIRSUP)) {
            if (this.map.equals(map3)) {
                nextMap(map2);
                Sound.GOINGUPDOWNSTAIRS.playSound("GoingUpDownStairs.wav");
                refresh();
            } else if (this.map.equals(map2)) {
                nextMap(map1);
                Sound.GOINGUPDOWNSTAIRS.playSound("GoingUpDownStairs.wav");
                refresh();
            }
        } else if (map.getPlayer().isNeighborCellType(0, 0, CellType.STAIRSDOWN)) {
            if (this.map.equals(map1)) {
                nextMap(map2);
                Sound.GOINGUPDOWNSTAIRS.playSound("GoingUpDownStairs.wav");
                refresh();
            } else if (this.map.equals(map2)) {
                nextMap(map3);
                Sound.GOINGUPDOWNSTAIRS.playSound("GoingUpDownStairs.wav");
                refresh();
            } else if (this.map.equals(map3)) {
                Canvas canvas = new Canvas(this.canvas.getWidth(), this.canvas.getHeight());
                this.borderPane.setCenter(canvas);
                this.borderPane.setRight(null);

                GraphicsContext context = canvas.getGraphicsContext2D();
                context.setFont(new Font("arial", 42));
                context.setFill(Color.BLACK);
                context.setTextAlign(TextAlignment.CENTER);
                context.fillText("You WIN!", canvas.getWidth() / 2, canvas.getHeight() / 2);
            }
        }
    }

    private void nextMap(GameMap nextMap) {
        int prevHealth;
        boolean prevCheater;
        Map<String, Integer> prevInventory;
        prevHealth = this.map.getPlayer().getHealth();
        prevInventory = this.map.getPlayer().getInventory();
        prevCheater = this.map.getPlayer().getCheater();
        this.map = nextMap;
        this.map.getPlayer().setHealth(prevHealth);
        this.map.getPlayer().setInventory(prevInventory);
        if (prevCheater) {
            this.map.getPlayer().setCheater();
        }
    }

    public static int randInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    private String namePopup() {
        TextInputDialog td = new TextInputDialog("Knighty McKnight");
        td.setHeaderText("enter your name");
        td.showAndWait();
        return td.getEditor().getText();
    }

    private String setUpPlayerName() {
        String playerName = namePopup();
        List<String> developers = new ArrayList<>(List.of("Ágoston", "Ákos", "Bálint", "Márk")){};
        if (developers.contains(playerName)) {
            map.getPlayer().setCheater();
        }
        return playerName;
    }
}
