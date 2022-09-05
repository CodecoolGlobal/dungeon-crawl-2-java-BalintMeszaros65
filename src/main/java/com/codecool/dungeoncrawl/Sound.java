package com.codecool.dungeoncrawl;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public enum Sound {
    MAIN_MENU,
    PLAY_GAME,
    MOVE,
    PUNCH,
    SWORD_DUEL,
    DAMAGE,
    DIE,
    HEAL_UP,
    OPEN_DOOR,
    GOING_UP_OR_DOWN_ON_STAIRS,
    PICK_UP_ITEM,
    ZOMBIE,
    GHOST,
    SKELETON;




    public void playSound(String soundFile) {
        try {
            File f = new File("sounds/" + soundFile);
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

    public String toString(Sound sound){
        switch (sound){
            case MAIN_MENU:
                return "MainMenu.wav";
            case PLAY_GAME:
                return "PlayGame.wav";
            case MOVE:
                return "Move.wav";
            case PUNCH:
                return "Punch.wav";
            case SWORD_DUEL:
                return "SwordDuel";
            case DAMAGE:
                return "Damage.wav";
            case DIE:
                return "Die.wav";
            case HEAL_UP:
                return "HealUp.wav";
            case OPEN_DOOR:
                return "OpenDoor.wav";
            case GOING_UP_OR_DOWN_ON_STAIRS:
                return "GoingUpDownStairs.wav";
            case PICK_UP_ITEM:
                return "PickUpItem.wav";
            case ZOMBIE:
                return "ZombieSound.wav";
            case GHOST:
                return "GhostSound.wav";
            case SKELETON:
                return "SkeletonSound.wav";
            default:
                return "Sound not found";
        }
    }
}
