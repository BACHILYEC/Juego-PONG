package com.adrian.DataAccess;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DB_FOLDER = "src/main/DataBase";
    private static final String DB_PATH = DB_FOLDER + "/matches.sqlite";
    private static final String URL = "jdbc:sqlite:" + DB_PATH;

    public static Connection getConnection() throws SQLException {

        File folder = new File(DB_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File dbFile = new File(DB_PATH);
        if (!dbFile.exists()) {
            try {
                dbFile.createNewFile();
            } catch (Exception e) {
                throw new SQLException("No se pudo crear la base de datos: " + e.getMessage());
            }
        }

        return DriverManager.getConnection(URL);
    }
}
