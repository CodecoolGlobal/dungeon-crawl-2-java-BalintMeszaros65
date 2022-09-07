package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameStateDaoJdbc implements GameStateDao {
    private DataSource dataSource;

    public GameStateDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(GameState state) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO game_state (saved_title, current_map, saved_at, player_id) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, state.getSavedTitle());
            statement.setString(2, state.getCurrentMap());
            statement.setDate(3, state.getSavedAt());
            statement.setInt(4, state.getPlayer().getId());
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
            String sqlGameState = "SELECT game_state.current_map, game_state.saved_at, game_state.player_id, " +
                    "player.player_name, player.hp, player.x, player.y " +
                    "FROM game_state " +
                    "INNER JOIN player ON player.id = game_state.player_id " +
                    "WHERE id = ?";
            PreparedStatement statementGameState = connection.prepareStatement(sqlGameState);
            statementGameState.setInt(1, id);
            ResultSet resultSet = statementGameState.executeQuery();

            PlayerModel player = new PlayerModel(resultSet.getString(5), resultSet.getInt(7), resultSet.getInt(8));
            player.setHp(resultSet.getInt(6));

            if (resultSet.next()) {
                return null;
            }
            return new GameState(resultSet.getString(2), resultSet.getDate(3), player);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GameState> getAll() {
        try (Connection connection = dataSource.getConnection()){
            List<GameState> gameStates = new ArrayList<>();

            String sqlGameState = "SELECT game_state.id, game_state.current_map, game_state.saved_at, game_state.player_id, " +
                    "player.player_name, player.hp, player.x, player.y " +
                    "FROM game_state " +
                    "INNER JOIN player ON player.id = game_state.player_id ";
            PreparedStatement statementGameState = connection.prepareStatement(sqlGameState);
            ResultSet resultSet = statementGameState.executeQuery();

            while (resultSet.next()){
                PlayerModel playerModel = new PlayerModel(resultSet.getString(5), resultSet.getInt(7), resultSet.getInt(8));
                playerModel.setHp(resultSet.getInt(6));
                gameStates.add(new GameState(resultSet.getString(2), resultSet.getDate(3), playerModel));
            }

            return gameStates;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }
}
