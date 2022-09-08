package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.SQLException;

public class GameDatabaseManager {


    private static PlayerDao playerDao;

    private static GameStateDao gameStateDao;

    public void setup() throws SQLException {
        DataSource dataSource = connect();
        playerDao = new PlayerDaoJdbc(dataSource);
        gameStateDao = new GameStateDaoJdbc(dataSource);
    }

    public void savePlayer(Player player) {
        PlayerModel model = new PlayerModel(player);
        playerDao.add(model);
        player.setId(model.getId());
    }

    public static GameState load(int id) {
        return gameStateDao.get(id);
    }

    public void saveGameState(int currentMap, String map1, String map2, String map3, PlayerModel playerModel){
        GameState gameState = new GameState(currentMap, map1, map2, map3, new Date(System.currentTimeMillis()), playerModel);
        gameState.setSavedTitle("Test");
        gameStateDao.add(gameState);
    }

    private DataSource connect() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        String dbName = System.getenv("PSQL_DB_NAME");
        String user = System.getenv("PSQL_USER_NAME");
        String password = System.getenv("PSQL_PASSWORD");

        dataSource.setDatabaseName(dbName);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        System.out.println("Trying to connect");
        dataSource.getConnection().close();
        System.out.println("Connection ok.");

        return dataSource;
    }

    public PlayerDao getPlayerDao() {
        return playerDao;
    }
    public GameStateDao getGameStateDao() {
        return gameStateDao;
    }
}
