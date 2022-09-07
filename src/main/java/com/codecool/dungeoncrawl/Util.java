package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.GameMap;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Util {
    static Random random = new Random();
    static final List<String> DEVELOPERS = new ArrayList<>(List.of("Ágoston", "Ákos", "Bálint", "Márk"));
    private static String namePopup() {
        TextInputDialog td = new TextInputDialog("Knighty McKnight");
        td.setHeaderText("enter your name");
        td.showAndWait();
        return td.getEditor().getText();
    }

    public static String setUpPlayerName(GameMap map) {
        String playerName = namePopup();
        if (DEVELOPERS.contains(playerName)) {
            map.getPlayer().setCheater();
        }
        map.getPlayer().setName(playerName);
        return playerName;
    }

    public static void youMessage(Color color, String message, double width, double height, BorderPane borderPane) {
        Canvas canvas = new Canvas(width, height);
        borderPane.setCenter(canvas);
        borderPane.setRight(null);
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFont(new Font("arial", 42));
        context.setFill(color);
        context.setTextAlign(TextAlignment.CENTER);
        context.fillText(message, canvas.getWidth() / 2, canvas.getHeight() / 2);
    }

    public static void playRandomEnemySoundEveryNTurns(int roundCounter, int n) {
        if (roundCounter % n == 0) {
            Sound[] enemiesSound = {Sound.ZOMBIE, Sound.GHOST, Sound.SKELETON};
            Sound pickedSound = enemiesSound[Util.randInt(0, 2)];
            pickedSound.playSound();
        }
    }

    public static int randInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }
}
