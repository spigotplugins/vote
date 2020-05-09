/*package io.github.portlek.vote.database;

import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class DatabaseHelper {

    @NotNull
    private final DatabaseAccess databaseAccess;

    public DatabaseHelper(@NotNull DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    public void createTable() {
        try(final Connection connection = databaseAccess.getConnection();
            final Statement statement = connection.createStatement()
        ){
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS votes(id BIGINT(64) NOT NULL AUTO_INCREMENT UNIQUE, uuid VARCHAR(32) NOT NULL, serverName VARCHAR(32) NOT NULL, unGivenRewards BIGINT(32) NOT NULL);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    public List<Integer> getIds(@NotNull UUID uuid) {
        final List<Integer> ids = new ArrayList<>();

        try(final Connection connection = databaseAccess.getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM votes WHERE uuid = ?");
        ) {
            preparedStatement.setString(1, uuid.toString());

            final ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ids.add(
                    resultSet.getInt("uuid")
                );
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ids;
    }

    @NotNull
    public String getServerName(int id) {
        try(final Connection connection = databaseAccess.getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT serverName FROM votes WHERE id = ?");
        ) {
            preparedStatement.setInt(1, id);

            final ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.getString("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "";
    }

    public int getUnGivenRewards(int id) {
        try(final Connection connection = databaseAccess.getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT unGivenReawrds FROM votes WHERE id = ?")
        ) {
            preparedStatement.setInt(1, id);

            final ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void delete(int id) {
        try(final Connection connection = databaseAccess.getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM votes WHERE id = ?")
        ) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateData(int id, int unGivenRewards) {
        try(final Connection connection = databaseAccess.getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("UPDATE votes SET unGivenRewards = ? WHERE id = ?")
        ) {
            preparedStatement.setInt(1, unGivenRewards);
            preparedStatement.setInt(2, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addData(@NotNull UUID uuid, int unGivenRewards, @NotNull String serverName) {
        try(final Connection connection = databaseAccess.getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO test(uuid, serverName, unGivenRewards) VALUES (?,?,?)")
        ) {
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, serverName);
            preparedStatement.setInt(3, unGivenRewards);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}*/