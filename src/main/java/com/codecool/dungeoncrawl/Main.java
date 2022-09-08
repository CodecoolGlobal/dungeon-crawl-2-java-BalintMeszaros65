package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.dao.GameStateDao;
import com.codecool.dungeoncrawl.dao.PlayerDao;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Direction;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.*;
import com.codecool.dungeoncrawl.logic.items.ClosedDoor;
import com.codecool.dungeoncrawl.logic.items.CoinChest;
import com.codecool.dungeoncrawl.logic.items.HealthPotion;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.*;

// TODO sounds
// TODO zoom
public class Main extends Application {
    GameMap map1 = MapLoader.loadMap("map1");
    GameMap map2 = MapLoader.loadMap("map2");
    GameMap map3 = MapLoader.loadMap("map3");
    static GameMap map;
    String playerName;
    private int roundCounter = 0;
    Canvas canvas;
    GraphicsContext context;
    GridPane ui = new GridPane();
    BorderPane borderPane;

    PlayerDao playerDao;
    GameStateDao gameStateDao;

    private GameDatabaseManager databaseManager;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //TODO
        //Util.PopupMenu();
        map = map1;
        playerName = Util.setUpPlayerName(map);
        Util.canvasWidth = map.getWidth() * Tiles.TILE_WIDTH;
        Util.canvasHeight = map.getHeight() * Tiles.TILE_WIDTH;
        canvas = new Canvas(Util.canvasWidth, Util.canvasHeight);
        canvas.setScaleY(1.5);
        canvas.setScaleX(1.5);
        context = canvas.getGraphicsContext2D();
        context.setImageSmoothing(false);
        map1.setNextMap(map2);
        map2.setPrevMap(map1);
        map2.setNextMap(map3);
        map3.setPrevMap(map2);
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));

        borderPane = new BorderPane();

        borderPane.setCenter(canvas);
        borderPane.setRight(ui);
        Util.borderPane = this.borderPane;
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);

        primaryStage.setTitle("Platypus Crawl");
        primaryStage.show();

        databaseManager = new GameDatabaseManager();
        try {
            databaseManager.setup();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        playerDao = databaseManager.getPlayerDao();
        gameStateDao = databaseManager.getGameStateDao();


    }


    private void onKeyPressed(KeyEvent keyEvent) {
        Player player = map.getPlayer();
        if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.S) {
            if (player.getId() == null) {
                databaseManager.savePlayer(player);
                player = map.getPlayer();
                int currentMap = 0;
                if (map == map1){
                    currentMap = 1;
                } else if (map == map2){
                    currentMap = 2;
                } else if (map == map3){
                    currentMap = 3;
                }
                databaseManager.saveGameState(currentMap, map1.toTxtFormat(), map2.toTxtFormat(), map3.toTxtFormat(), playerDao.get(player.getId()));
            } else {
                PlayerModel playerModel = playerDao.get(player.getId());
                playerDao.update(playerModel);
            }


        } else if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.L) {
            // TODO load without hardcoded id
            GameState gameState = GameDatabaseManager.load(2);
            PlayerModel playerModel = gameState.getPlayer();
            map1 = MapLoader.loadMap(gameState, 1);
            map2 = MapLoader.loadMap(gameState, 2);
            map3 = MapLoader.loadMap(gameState, 3);
            map1.setNextMap(map2);
            map2.setPrevMap(map1);
            map2.setNextMap(map3);
            map3.setPrevMap(map2);
            switch (gameState.getCurrentMap()) {
                case 1:
                    map = map1;
                case 2:
                    map = map2;
                case 3:
                    map = map3;
            }
            Player newPlayer = map.getPlayer();
            newPlayer.setName(playerModel.getPlayerName());
            newPlayer.setHealth(playerModel.getHp());
            newPlayer.setNoClip(playerModel.isNoClip());
            playerName = playerModel.getPlayerName();
            refresh();
        } else {
            if (player.isAlive()) {
                switch (keyEvent.getCode()) {
                    case Q -> healPlayerAndMakeTurn(player);
                    case W -> movePlayerAndMakeTurn(player, Direction.NORTH);
                    case S -> movePlayerAndMakeTurn(player, Direction.SOUTH);
                    case A -> movePlayerAndMakeTurn(player, Direction.WEST);
                    case D -> movePlayerAndMakeTurn(player, Direction.EAST);
                    // TODO exit from game and/or program
                    case ESCAPE -> System.out.println("Implement me!");
                    case SPACE -> attackWithPlayerAndMakeTurn(player);
                }


                player.updateIsAlive();
            } else {
                Util.youMessage(Color.INDIANRED, "You died!");
            }
            Sound.playRandomEnemySoundEveryNTurns(roundCounter);
        }
    }

    private void attackWithPlayer(Player player) {
        int dRow = player.getDirection().getDirectionDRow();
        int dCol = player.getDirection().getDirectionDCol();
        if (player.isNeighborActor(dCol, dRow)) {
            player.attack(dCol, dRow);
        }
    }

    private void attackWithPlayerAndMakeTurn(Player player) {
        attackWithPlayer(player);
        makeTurn();
    }

    private void movePlayer(Player player, Direction direction) {
        int dRow = direction.getDirectionDRow();
        int dCol = direction.getDirectionDCol();
        int distance = player.getDistance();
        if (player.validateMove(dCol * distance, dRow * distance)) {
            player.move(dCol * distance, dRow * distance);
        }
        player.setDirection(direction);
    }

    private void movePlayerAndMakeTurn(Player player, Direction direction) {
        movePlayer(player, direction);
        makeTurn();
    }

    private void healPlayer(Player player) {
        if (player.getInventory().containsKey("health-potion")) {
            player.healUp(HealthPotion.getHealsAmount());
            player.removeInventoryItem("health-potion");
        }
    }

    private void healPlayerAndMakeTurn(Player player) {
        healPlayer(player);
        makeTurn();
    }

    private void makeTurn() {
        actWithEnemies();
        roundCounter++;
        refresh();
    }

    private void actWithEnemies() {
        List<Actor> enemies = map.getEnemies();
        List<Actor> enemiesToBeRemoved = new ArrayList<>();
        if (enemies != null && !enemies.isEmpty()) {
            enemies.forEach(enemy -> {
                        if (enemy.isAlive()) {
                            actWithAliveEnemy(enemy);
                        } else {
                            actWithDeadEnemy(enemy, enemiesToBeRemoved);
                        }
                    }
            );
            enemiesToBeRemoved.forEach(enemies::remove);
        }
    }

    private void actWithAliveEnemy(Actor enemy) {
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
    }

    private void actWithDeadEnemy(Actor enemy, List<Actor> enemiesToBeRemoved) {
        enemy.getCell().setActor(null);
        enemiesToBeRemoved.add(enemy);
    }


    private void refresh() {
        int xDif = 5;
        int yDif = 4;
        int startX = getStartPosition(map.getPlayer().getX(), xDif, map.getWidth());
        int startY = getStartPosition(map.getPlayer().getY(), yDif, map.getHeight());
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int x = startX - xDif; x < startX + xDif + 1; x++) {
            for (int y = startY - yDif; y < startY + yDif + 1; y++) {
                Cell cell = map.getCell(x, y);
                if (cell.hasActor()) {
                    Tiles.drawTile(context, cell.getActor(), x - startX + xDif, y - startY + yDif);
                } else if (cell.hasItem()) {
                    Tiles.drawTile(context, cell.getItem(), x - startX + xDif, y - startY + yDif);
                } else {
                    Tiles.drawTile(context, cell, x - startX + xDif, y - startY + yDif);
                }
            }
        }
        fillGridPane();
    }

    private int getStartPosition(int position, int difference, int max) {
        int startX;
        if (position - difference < 0) {
            startX = difference;
        } else if (position + difference > max - 1) {
            startX = max - difference - 1;
        } else {
            startX = position;
        }
        return startX;
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
            Button itemButton = null;
            if (Objects.equals(item.getTileName(), "coin-chest")) {
                itemButton = new Button("Open chest");
            } else if (!Objects.equals(item.getTileName(), "opened-chest")) {
                itemButton = new Button("Pick up " + item.getTileName());
            }
            if (itemButton != null) {
                ui.add(itemButton, 0, positionOnUI);
                itemButton.setOnAction(event -> {
                    if (!Objects.equals(item.getTileName(), "coin-chest")) {
                        player.putItemToInventory(item.getTileName());
                        player.getCell().setItem(null);
                    } else {
                        for (int i = 0; i < ((CoinChest) item).getNumberOfCoins(); i++) {
                            player.putItemToInventory("coin");
                        }
                        ((CoinChest) item).lootChest();
                    }
                    refresh();
                });
            }
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
        } catch (IndexOutOfBoundsException ignore) {
        }
        if (closedDoorPosition.length == 2 && player.getInventory().containsKey("key")) {
            Button doorButton = new Button("Open door!");
            ui.add(doorButton, 0, positionOnUI);
            int[] finalClosedDoorPosition = closedDoorPosition;
            doorButton.setOnAction(event -> {
                player.removeInventoryItem("key");
                map.getCell(player.getX() + finalClosedDoorPosition[0],
                        player.getY() + finalClosedDoorPosition[1]).setItem(null);
                Sound.OPEN_DOOR.playSound();
                refresh();
            });
        }
    }

    public static void changeMap(GameMap nextMap, Player player) {
        int prevHealth;
        boolean prevCheater;
        Map<String, Integer> prevInventory;
        prevHealth = player.getHealth();
        prevInventory = player.getInventory();
        prevCheater = player.getCheater();
        nextMap.getPlayer().setHealth(prevHealth);
        nextMap.getPlayer().setInventory(prevInventory);
        if (prevCheater) {
            nextMap.getPlayer().setCheater();
        }
        map = nextMap;
    }
}
