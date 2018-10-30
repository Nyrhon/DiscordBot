package de.karmell.discord.bot.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;

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
                "AutoClear INTEGER(1) NOT NULL," +
                "Exclusive INTEGER(1) NOT NULL )");
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
            ArrayList<DedicatedChannel> dedicatedChannels = new ArrayList<>();
            while(result.next()) {
                DedicatedChannel dc = new DedicatedChannel();
                dc.setChannelId(result.getLong("ChannelID"));
                dc.setAutoClear(result.getInt("AutoClear") == 1);
                dc.setExclusive(result.getInt("Exclusive") == 1);
                dedicatedChannels.add(dc);
            }
            guildWrapper.setDedicatedChannels(dedicatedChannels);
        } catch (SQLException e) {
            log.error("Could not get bot channels for guildId: " + guildId, e);
        }
    }
}
