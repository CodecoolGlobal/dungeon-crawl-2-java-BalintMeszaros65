package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class GameStateDaoJdbc implements GameStateDao {
    private DataSource dataSource;

    public GameStateDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(GameState state) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO game_state (current_map, saved_at, player_id) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, state.getCurrentMap());
            statement.setDate(2, state.getSavedAt());
            statement.setInt(3, state.getPlayer().getId());
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
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE game_state SET current_map = ?, saved_at = ? WHERE player_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, state.getCurrentMap());
            statement.setDate(2, state.getSavedAt());
            statement.setInt(3, state.getPlayer().getId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            state.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameState get(int id) {
        try (Connection connection = dataSource.getConnection()) {
            String sqlGameState = "SELECT id, current_map, saved_at, player_id FROM game_state WHERE id = ?";
            PreparedStatement statementGameState = connection.prepareStatement(sqlGameState);
            statementGameState.setInt(1, id);
            ResultSet resultSetGameState = statementGameState.executeQuery();

            String sqlPlayer = "SELECT id, player_name, hp, x, y FROM player WHERE id = ?";
            PreparedStatement statementPlayer = connection.prepareStatement(sqlPlayer);
            statementPlayer.setInt(1, resultSetGameState.getInt(1));
            ResultSet resultSetPlayer = statementPlayer.executeQuery();

            if (resultSetPlayer.next()){
                return null;
            }
            PlayerModel player = new PlayerModel(resultSetPlayer.getString(2), resultSetPlayer.getInt(4), resultSetPlayer.getInt(5));
            player.setHp(resultSetPlayer.getInt(3));

            if (resultSetGameState.next()) {
                return null;
            }
            GameState gameState = new GameState(resultSetGameState.getString(2), resultSetGameState.getDate(3), player);
            return gameState;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GameState> getAll() {
        return null;
    }
}
