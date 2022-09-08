package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.GameMap;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Util {
    static double canvasWidth;
    static double canvasHeight;
    static BorderPane borderPane;
    static Random random = new Random();
    static final List<String> DEVELOPERS = new ArrayList<>(List.of("Ákos", "Bálint", "Márk"));
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

    public static void youMessage(Color color, String message) {
        Canvas canvas = new Canvas(canvasWidth, canvasHeight);
        borderPane.setCenter(canvas);
        borderPane.setRight(null);
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFont(new Font("arial", 42));
        context.setFill(color);
        context.setTextAlign(TextAlignment.CENTER);
        context.fillText(message, canvas.getWidth() / 2, canvas.getHeight() / 2);
    }

    public static int randInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    public static void PopupMenu(){
        final JFrame f= new JFrame("Main Menu");
        final JPopupMenu mainMenu = new JPopupMenu("MainMenu");
        JMenuItem playMenu = new JMenuItem("Play");
        JMenuItem loadMenu = new JMenuItem("Load");
        //playMenu.setAction();
        mainMenu.add(playMenu);
        mainMenu.add(loadMenu);
        f.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mainMenu.show(f , e.getX(), e.getY());
            }
        });
        f.add(mainMenu);
        f.setSize(300,300);
        f.setLayout(null);
        f.setVisible(true);
    }
}
