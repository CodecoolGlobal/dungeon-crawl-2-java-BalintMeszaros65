package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDaoJdbc implements PlayerDao {
    int PLAYER_NAME = 1;
    int PLAYER_HP = 2;
    int PLAYER_X = 3;
    int PLAYER_Y = 4;
    int PLAYER_NO_CLIP = 5;
    int PLAYER_ID = 6;
    private DataSource dataSource;

    public PlayerDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(PlayerModel player) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = """
            INSERT INTO player (player_name, hp, x, y, no_clip)
            VALUES (?, ?, ?, ?, ?)
            """;
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(PLAYER_NAME, player.getPlayerName());
            statement.setInt(PLAYER_HP, player.getHp());
            statement.setInt(PLAYER_X, player.getX());
            statement.setInt(PLAYER_Y, player.getY());
            statement.setBoolean(PLAYER_NO_CLIP, player.isNoClip());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            player.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(PlayerModel player) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = """
            UPDATE player
            SET player_name = ?, hp = ?, x = ?, y = ?, no_clip = ?
            WHERE id = ?
            """;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(PLAYER_NAME, player.getPlayerName());
            statement.setInt(PLAYER_HP, player.getHp());
            statement.setInt(PLAYER_X, player.getX());
            statement.setInt(PLAYER_Y, player.getY());
            statement.setBoolean(PLAYER_NO_CLIP, player.isNoClip());
            statement.setInt(PLAYER_ID, player.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PlayerModel get(int id) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = """
            SELECT player_name, hp, x, y, no_clip
            FROM player
            WHERE id = ?
            """;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            PlayerModel playerModel = new PlayerModel(resultSet.getString(PLAYER_NAME), resultSet.getInt(PLAYER_HP),
                    resultSet.getInt(PLAYER_X), resultSet.getInt(PLAYER_Y), resultSet.getBoolean(PLAYER_NO_CLIP));
            playerModel.setId(id);
            return playerModel;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PlayerModel> getAll() {
        try (Connection connection = dataSource.getConnection()) {
            List<PlayerModel> playerModels = new ArrayList<>();
            String sql = """
            SELECT player_name, hp, x, y, no_clip
            FROM player
            """;
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                playerModels.add(new PlayerModel(resultSet.getString(PLAYER_NAME), resultSet.getInt(PLAYER_HP),
                        resultSet.getInt(PLAYER_X), resultSet.getInt(PLAYER_Y), resultSet.getBoolean(PLAYER_NO_CLIP)));
            }

            return playerModels;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
