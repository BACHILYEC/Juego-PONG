package com.adrian.DataAccess;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DB_FOLDER = "DataBase";
    private static final String DB_PATH = DB_FOLDER + "/matches.sqlite";
    private static final String URL = "jdbc:sqlite:" + DB_PATH;

    public static Connection getConnection() throws SQLException {
        File folder = new File(DB_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        boolean isNew = !new File(DB_PATH).exists();

        Connection conn = DriverManager.getConnection(URL);

        if (isNew) {
            createTable(conn);
        }

        return conn;
    }

    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS matches (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "player1 TEXT NOT NULL," +
                "player2 TEXT NOT NULL," +
                "player1_score INTEGER NOT NULL," +
                "player2_score INTEGER NOT NULL," +
                "duration_seconds INTEGER NOT NULL," +
                "date DATETIME DEFAULT (datetime('now', 'localtime'))" +
                ");";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }
}
