package de.karmell.discord.bot.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseWrapper {
    private Connection connection;

    public DatabaseWrapper() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:bot.db");
    }
}
