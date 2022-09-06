package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.GameMap;
import javafx.scene.control.TextInputDialog;

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

    public static int randInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }
}
