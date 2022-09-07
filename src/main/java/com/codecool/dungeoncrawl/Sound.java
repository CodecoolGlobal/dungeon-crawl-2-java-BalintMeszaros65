package com.codecool.dungeoncrawl;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public enum Sound {
    MAIN_MENU("MainMenu.wav"),
    PLAY_GAME("PlayGame.wav"),
    MOVE("Move.wav"),
    PUNCH("Punch.wav"),
    SWORD_DUEL("SwordDuel"),
    DAMAGE("Damage.wav"),
    DIE("Die.wav"),
    HEAL_UP("HealUp.wav"),
    OPEN_DOOR("OpenDoor.wav"),
    GOING_UP_OR_DOWN_ON_STAIRS("GoingUpDownStairs.wav"),
    PICK_UP_ITEM("PickUpItem.wav"),
    ZOMBIE("ZombieSound.wav"),
    GHOST("GhostSound.wav"),
    SKELETON("SkeletonSound.wav");

    private final String soundText;
    Sound(String string) {
        this.soundText = string;
    }

    public void playSound() {
        try {
            File f = new File("sounds/" + this.soundText);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP)
                    clip.close();
            });
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException ignored){}

    }
}
