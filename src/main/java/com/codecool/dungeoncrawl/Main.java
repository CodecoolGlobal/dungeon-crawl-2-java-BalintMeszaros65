package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
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

public class Main extends Application {
    GameMap map1 = MapLoader.loadMap("map1");
    GameMap map2 = MapLoader.loadMap("map2");
    GameMap map3 = MapLoader.loadMap("map3");
    GameMap map = map1;
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    Label healthLabel = new Label();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane ui = new GridPane();
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));

        ui.add(new Label("Health: "), 0, 0);
        ui.add(healthLabel, 1, 0);

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
        switch (keyEvent.getCode()) {
            // TODO move validation (instance of?)
            // TODO pickup item (mouseclick)
            case W:
                map.getPlayer().move(0, -1);
                moveEnemies();
                refresh();
                break;
            case S:
                map.getPlayer().move(0, 1);
                moveEnemies();
                refresh();
                break;
            case A:
                map.getPlayer().move(-1, 0);
                moveEnemies();
                refresh();
                break;
            case D:
                map.getPlayer().move(1,0);
                moveEnemies();
                refresh();
                break;
        }
    }

    private void moveEnemies() {
        // TODO enemy checking neighbors for player + if true damage
        // TODO enemy movement method
        List<Actor> enemies = map.getEnemies();
        if (enemies != null) {
            for (Actor enemy : enemies) {
                if (enemy instanceof Skeleton) {
                    // enemy.move

                }
            }
        }
    }

    private void refresh() {
        changeMap();
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
        healthLabel.setText("Health: " + map.getPlayer().getHealth());
    }

    private void changeMap() {
        int prevHealth;
        if (map.getPlayer().getCell().getTileName().equals("stairs-up")) {
            if (this.map.equals(map2)) {
                prevHealth = this.map.getPlayer().getHealth();
                this.map = map1;
                this.map.getPlayer().setHealth(prevHealth);
            } else if (this.map.equals(map3)) {
                prevHealth = this.map.getPlayer().getHealth();
                this.map = map2;
                this.map.getPlayer().setHealth(prevHealth);
            }
        } else if (map.getPlayer().getCell().getTileName().equals("stairs-down")) {
            if (this.map.equals(map1)) {
                prevHealth = this.map.getPlayer().getHealth();
                this.map = map2;
                this.map.getPlayer().setHealth(prevHealth);
            } else if (this.map.equals(map2)) {
                prevHealth = this.map.getPlayer().getHealth();
                this.map = map3;
                this.map.getPlayer().setHealth(prevHealth);
            }
        }
    }

    // TODO randint
}
