package de.karmell.discord.bot.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                "AutoClear INTEGER(1) NOT NULL)");
        statement.executeUpdate("create table GuildSettings (" +
                "GuildID INTEGER(8) NOT NULL," +
                "ExclusiveMode INTEGER(1) NOT NULL DEFAULT 0)");
        statement.executeUpdate("create table DisabledCommands (" +
                "GuildID INTEGER(8) NOT NULL," +
                "Invoke VARCHAR NOT NULL)");
        statement.executeUpdate("create table CommandSettings (" +
                "GuildID INTEGER(8) NOT NULL," +
                "Invoke VARCHAR NOT NULL," +
                "RoleID INTEGER(8) NOT NULL)");
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
                dedicatedChannels.add(dc);
            }
            guildWrapper.setDedicatedChannels(dedicatedChannels);
            statement.close();
            Statement statement2 = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet result2 = statement2.executeQuery("select * from GuildSettings where GuildID = " + guildId);
            if(result2.next()) {
                guildWrapper.setExclusiveMode(result2.getInt("ExclusiveMode") == 1);
            }
            statement2.close();
        } catch (SQLException e) {
            log.error("Could not get bot channels for guildId: " + guildId, e);
        }
    }

    public void initDisabledCommands(long guildId, GuildWrapper guildWrapper) {
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet result = statement.executeQuery("select * from DisabledCommands where GuildID = " + guildId);
            List<String> disabledCommands = new ArrayList<>();
            while(result.next()) {
                disabledCommands.add(result.getString("Invoke"));
            }
            guildWrapper.setDisabledCommands(disabledCommands);
            statement.close();
        } catch (SQLException e) {
            log.error("Could not init disabled commands for guildId: " + guildId, e);
        }
    }

    public void initAccessibleRoles(long guildId, GuildWrapper guildWrapper) {
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet result = statement.executeQuery("select * from CommandSettings where GuildID = " + guildId);
            Map<String, List<Long>> accessibleRoles = new HashMap<>();
            while(result.next()) {
                String invoke = result.getString("Invoke");
                long roleID = result.getLong("RoleID");
                if(!accessibleRoles.containsKey(invoke)) {
                    accessibleRoles.put(invoke, new ArrayList<>());
                }
                accessibleRoles.get(invoke).add(roleID);
            }
            guildWrapper.setAccessibleRoles(accessibleRoles);
            statement.close();
        } catch (SQLException e) {
            log.error("Could not init accessible roles for guildId: " + guildId, e);
        }
    }

    public void addBotChannel(long guildID, DedicatedChannel dc) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("insert into BotChannels (GuildID, ChannelID, AutoClear)" +
                    "values (" + guildID + ", " + dc.getChannelId() + ", 0)");
            statement.close();
        } catch (SQLException e) {
            log.error("Could not add bot channel " + dc.getChannelId(), e);
        }
    }

    public void removeBotChannel(long guildID, long channelID) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("delete from BotChannels where GuildID = " + guildID + " and ChannelID = " + channelID);
            statement.close();
        } catch (SQLException e) {
            log.error("Could not remove bot channel " + channelID, e);
        }
    }

    public void addAccessibleRole(long guildID, String invoke, long roleID) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("insert into CommandSettings (GuildID, Invoke, RoleID)" +
                    "values (" + guildID + ", '" + invoke + "', " + roleID + ")");
            statement.close();
        } catch (SQLException e) {
            log.error("Could not add accessible role " + roleID + " for " + guildID, e);
        }
    }

    public void removeAccessibleRole(long guildID, String invoke, long roleID) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("delete from CommandSettings where GuildID = "
                    + guildID + " and Invoke = '" + invoke + "' and RoleID = " + roleID);
            statement.close();
        } catch (SQLException e) {
            log.error("Could not remove accesible role.", e);
        }
    }

    public void saveGuildSettings(long guildID, boolean exclusiveMode) {
        try {
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate("update GuildSettings set ExclusiveMode = " + (exclusiveMode ? 1 : 0)
                    + " where GuildID = " + guildID);
            if(result == 0) {
                statement.executeUpdate("insert into GuildSettings (GuildID, ExclusiveMode) values (" + guildID + ", " + (exclusiveMode ? 1 : 0) + ")");
            }
            statement.close();
        } catch (SQLException e) {
            log.error("Could not save guild settings.", e);
        }
    }

    public void updateBotChannel(long guildID, long channelID, boolean autoClear) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("update BotChannels set AutoClear = "
                    + (autoClear ? 1 : 0) + " where GuildID = " + guildID + " and ChannelID = " + channelID);
            statement.close();
        } catch (SQLException e) {
            log.error("Could not update bot channel.", e);
        }
    }

    public void addDisabledCommand(long guildID, String invoke) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("insert into DisabledCommands (GuildID, Invoke) " +
                    "values (" + guildID + ", '" + invoke + "')");
            statement.close();
        } catch (SQLException e) {
            log.error("Could not add disabled command.", e);
        }
    }

    public void removeDisabledCommand(long guildID, String invoke) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("delete from DisabledCommands where GuildID = " + guildID + " and Invoke = '" + invoke + "'");
            statement.close();
        } catch (SQLException e) {
            log.error("Could not remove disabled command.", e);
        }
    }
}
