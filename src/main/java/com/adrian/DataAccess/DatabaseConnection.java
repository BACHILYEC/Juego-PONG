package com.adrian.DataAccess;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DB_FOLDER;
    private static final String DB_PATH;
    private static final String URL;

    static {
        String appData = System.getenv("APPDATA");
        String basePath;

        if (appData != null && !appData.isEmpty()) {
            basePath = appData + File.separator + "PingPong";
        } else {
            basePath = System.getProperty("user.home") + File.separator + ".pingpong";
        }

        DB_FOLDER = basePath + File.separator + "DataBase";
        DB_PATH = DB_FOLDER + File.separator + "matches.sqlite";
        URL = "jdbc:sqlite:" + DB_PATH;

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            logToFile("[DB] Driver JDBC no encontrado: " + e.getMessage());
        }

        logToFile("[DB] Ruta base: " + basePath);
        logToFile("[DB] Ruta BD: " + DB_PATH);
    }

    public static String getDbPath() {
        return DB_PATH;
    }

    public static Connection getConnection() throws SQLException {
        File folder = new File(DB_FOLDER);
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            logToFile("[DB] Carpeta creada: " + created + " -> " + folder.getAbsolutePath());
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
                logToFile("[DB] Error cerrando conexión: " + e.getMessage());
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

    // Como es una app con ventana, no ves la consola: escribe un log a archivo
    private static void logToFile(String message) {
        try {
            File logFile = new File(DB_FOLDER != null ? DB_FOLDER : ".", "debug.log");
            logFile.getParentFile().mkdirs();
            try (java.io.FileWriter fw = new java.io.FileWriter(logFile, true)) {
                fw.write(message + System.lineSeparator());
            }
        } catch (Exception ignored) {
            // último recurso: no hacer nada
        }
    }
}