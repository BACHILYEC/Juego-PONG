package com.adrian.DataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MatchDAO {

    private final Connection connection;

    public MatchDAO(Connection connection) {
        this.connection = connection;
    }

    public void save(MatchDTO match) throws SQLException {

        String sql = "INSERT INTO matches (player1, player2, player1_score, player2_score, duration_seconds) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, match.getPlayer1());
            statement.setString(2, match.getPlayer2());
            statement.setInt(3, match.getPlayer1Score());
            statement.setInt(4, match.getPlayer2Score());
            statement.setLong(5, match.getDurationSeconds());

            statement.executeUpdate();
        }
    }

    public List<MatchDTO> findAll() throws SQLException {

        String sql = "SELECT id, player1, player2, player1_score, player2_score, duration_seconds, date FROM matches";

        List<MatchDTO> matches = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                MatchDTO match = new MatchDTO(
                        result.getInt("id"),
                        result.getString("player1"),
                        result.getString("player2"),
                        result.getInt("player1_score"),
                        result.getInt("player2_score"),
                        result.getLong("duration_seconds"),
                        result.getString("date"));

                matches.add(match);
            }
        }

        return matches;
    }

    public void delete() throws SQLException {

        String sql = "DELETE FROM matches";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.executeUpdate();
        }
    }
}