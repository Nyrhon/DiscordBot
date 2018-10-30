package de.karmell.discord.bot.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseWrapper {
    private Connection connection;
    private final Logger log = LogManager.getLogger(DatabaseWrapper.class);

    public DatabaseWrapper() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:bot.db");
    }

    public void init() throws SQLException{
        Statement statement = connection.createStatement();
        statement.executeUpdate("create table BotChannels (" +
                "GuildID  INTEGER(8) NOT NULL," +
                "ChannelID INTEGER(8) NOT NULL," +
                "AutoClear INTEGER(1) NOT NULL )");
        statement.executeUpdate("create table RoleRestrictions (" +
                "GuildID INTEGER(8) NOT NULL," +
                "Invoke VARCHAR NOT NULL," +
                "RoleID INTEGER(8) NOT NULL)");
        statement.executeUpdate("create table DisabledCommands (" +
                "GuildID INTEGER(8) NOT NULL," +
                "Invoke VARCHAR NOT NULL)");
        statement.close();
    }

    public void initBotChannels(long guildId, GuildWrapper guildWrapper) {
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet result = statement.executeQuery("select * from BotChannels where GuildID = " + guildId);
            long[] channelIDs = new long[result.getFetchSize()];
            boolean[] autoClear = new boolean[result.getFetchSize()];
            int i = 0;
            while(result.next()) {
                channelIDs[i] = result.getLong("ChannelID");
                autoClear[i] = result.getInt("AutoClear") == 1;
                i++;
            }
            guildWrapper.setDedicatedChannels(channelIDs);
            guildWrapper.setAutoClear(autoClear);
        } catch (SQLException e) {
            log.error("Could not get bot channels for guildId: " + guildId, e);
        }
    }
}
