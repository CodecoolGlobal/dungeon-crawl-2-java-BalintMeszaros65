package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameStateDaoJdbc implements GameStateDao {
    int TITLE = 1;
    int CURRENT_MAP = 2;
    int MAP1 = 3;
    int MAP2 = 4;
    int MAP3 = 5;
    int SAVED_AT = 6;
    int PLAYER_ID = 7;
    int PLAYER_NAME = 8;
    int PLAYER_HP = 9;
    int PLAYER_X = 10;
    int PLAYER_Y = 11;
    int PLAYER_NO_CLIP = 12;
    private DataSource dataSource;

    public GameStateDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(GameState state) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = """
            INSERT INTO game_state (saved_title, current_map, map1, map2, map3, saved_at, player_id)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(TITLE, state.getSavedTitle());
            statement.setInt(CURRENT_MAP, state.getCurrentMap());
            statement.setString(MAP1, state.getMap1());
            statement.setString(MAP2, state.getMap2());
            statement.setString(MAP3, state.getMap3());
            statement.setDate(SAVED_AT, state.getSavedAt());
            statement.setInt(PLAYER_ID, state.getPlayer().getId());
            statement.executeUpdate();
            ResultSet result = statement.getGeneratedKeys();
            result.next();
            state.setId(result.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(GameState state) {
        // TODO needs to be redone

//        try (Connection connection = dataSource.getConnection()) {
//            String sql = """
//            UPDATE game_state
//            SET saved_title = ?, current_map = ?, saved_at = ?
//            WHERE player_id = ?
//            """;
//            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setString(1, state.getSavedTitle());
//            statement.setInt(2, state.getCurrentMap());
//            statement.setDate(3, state.getSavedAt());
//            statement.setInt(4, state.getPlayer().getId());
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public GameState get(int id) {
        try (Connection connection = dataSource.getConnection()) {
            String sqlGameState = """
                SELECT saved_title, current_map, map1, map2, map3, saved_at, player_id,
                player.player_name, player.hp, player.x, player.y, player.no_clip
                FROM game_state
                INNER JOIN player ON player.id = game_state.player_id
                WHERE game_state.id = ?
                """;
            PreparedStatement statementGameState = connection.prepareStatement(sqlGameState);
            statementGameState.setInt(1, id);
            ResultSet resultSet = statementGameState.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            PlayerModel playerModel = new PlayerModel(resultSet.getString(PLAYER_NAME), resultSet.getInt(PLAYER_HP),
                    resultSet.getInt(PLAYER_X), resultSet.getInt(PLAYER_Y), resultSet.getBoolean(PLAYER_NO_CLIP));
            playerModel.setId(id);
            GameState gameState = new GameState(resultSet.getInt(CURRENT_MAP), resultSet.getString(MAP1),
                    resultSet.getString(MAP2), resultSet.getString(MAP3), resultSet.getDate(SAVED_AT), playerModel);
            gameState.setId(id);
            return gameState;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GameState> getAll() {
        try (Connection connection = dataSource.getConnection()) {
            List<GameState> gameStates = new ArrayList<>();

            String sqlGameState = """
                SELECT saved_title, current_map, map1, map2, map3, saved_at, player_id,
                player.player_name, player.hp, player.x, player.y, player.no_clip, game_state.id
                FROM game_state
                INNER JOIN player ON player.id = game_state.player_id
                """;
            PreparedStatement statementGameState = connection.prepareStatement(sqlGameState);
            ResultSet resultSet = statementGameState.executeQuery();

            if (!resultSet.next()) {
                return null;
            }
// TODO needs to be redone

//            while (resultSet.next()) {
//                PlayerModel player = new PlayerModel(resultSet.getString(9), resultSet.getInt(11), resultSet.getInt(12));
//                player.setId(resultSet.getInt(8));
//                player.setHp(resultSet.getInt(10));
//                GameState gameState = new GameState(resultSet.getInt(3), resultSet.getString(4),
//                        resultSet.getString(5), resultSet.getString(6), resultSet.getDate(7), player);
//                gameState.setId(resultSet.getInt(1));
//                gameStates.add(gameState);
//            }

            return gameStates;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
