package com.codecool.dungeoncrawl.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class GameState extends BaseModel {
    private Date savedAt;
    private int currentMap;
    private List<String> discoveredMaps = new ArrayList<>();
    private PlayerModel player;
    private String savedTitle;

    private String map1;
    private String map2;
    private String map3;
    public GameState(int currentMap, String map1, String map2, String map3, Date savedAt, PlayerModel player) {
        super();
        this.currentMap = currentMap;
        this.savedAt = savedAt;
        this.player = player;
        this.map1 = map1;
        this.map2 = map2;
        this.map3 = map3;

    }

    public Date getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(Date savedAt) {
        this.savedAt = savedAt;
    }

    public int getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(int currentMap) {
        this.currentMap = currentMap;
    }

    public List<String> getDiscoveredMaps() {
        return discoveredMaps;
    }

    public void addDiscoveredMap(String map) {
        this.discoveredMaps.add(map);
    }

    public PlayerModel getPlayer() {
        return player;
    }

    public void setPlayer(PlayerModel player) {
        this.player = player;
    }

    public String getSavedTitle() {
        return savedTitle;
    }

    public void setSavedTitle(String savedTitle) {
        this.savedTitle = savedTitle;
    }

    public String getMap1() {
        return map1;
    }

    public String getMap2() {
        return map2;
    }

    public String getMap3() {
        return map3;
    }
}
