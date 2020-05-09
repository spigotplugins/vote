/*package io.github.portlek.vote.database;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public final class DatabaseAccess {

    private static final ArrayList<Connection> POOL = new ArrayList<>();

    private static final int MAX_CONNECTIONS = 8;

    @NotNull
    private final String url;

    @NotNull
    private final Properties info;

    public DatabaseAccess(@NotNull String host, int port, @NotNull String database, @NotNull String username,
                          @NotNull String password) {
        info = new Properties();
        info.setProperty("autoReconnect", "true");
        info.setProperty("user", username);
        info.setProperty("password", password);
        info.setProperty("useUnicode", "true");
        info.setProperty("characterEncoding", "utf8");
        info.setProperty("useSSL", String.valueOf(false));
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        for (int i = 0; i < MAX_CONNECTIONS; i++) {
            POOL.add(null);
        }
    }

    @Nullable
    public Connection getConnection() {
        for (int i = 0; i < MAX_CONNECTIONS; i++) {
            Connection connection = POOL.get(i);
            try {
                if (connection != null && !connection.isClosed() && connection.isValid(10)) {
                    return connection;
                }
                connection = DriverManager.getConnection(url, info);

                POOL.set(i, connection);
                return connection;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}*/