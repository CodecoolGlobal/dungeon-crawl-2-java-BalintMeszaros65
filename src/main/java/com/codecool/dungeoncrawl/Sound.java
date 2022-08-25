package com.codecool.dungeoncrawl;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public enum Sound {
    MAINMENU,
    PLAYGAME,
    MOVE,
    PUNCH,
    SWORDDUEL,
    DAMAGE,
    DIE,
    HEALHUP,
    OPENDOOR,
    GOINGUPDOWNSTAIRS,
    PICKUPITEM,
    ZOMBIESOUND,
    GHOSTSOUND,
    SKELETONSOUND;




    public void playSound(String soundFile) {
        try {
            File f = new File("sounds/" + soundFile);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException ignored){}

    }

    public String toString(Sound sound){
        switch (sound){
            case MAINMENU:
                return "MainMenu.wav";
            case PLAYGAME:
                return "PlayGame.wav";
            case MOVE:
                return "Move.wav";
            case PUNCH:
                return "Punch.wav";
            case SWORDDUEL:
                return "SwordDuel";
            case DAMAGE:
                return "Damage.wav";
            case DIE:
                return "Die.wav";
            case HEALHUP:
                return "HealUp.wav";
            case OPENDOOR:
                return "OpenDoor.wav";
            case GOINGUPDOWNSTAIRS:
                return "GoingUpDownStairs.wav";
            case PICKUPITEM:
                return "PickUpItem.wav";
            case ZOMBIESOUND:
                return "ZombieSound.wav";
            case GHOSTSOUND:
                return "GhostSound.wav";
            case SKELETONSOUND:
                return "SkeletonSound.wav";
            default:
                return "Sound not found";
        }
    }
}
