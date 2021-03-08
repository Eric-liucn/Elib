package com.github.ericliucn.ericlib.handler;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.sql.SqlService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

public abstract class DatabaseHandlerBase {

    public String path;
    private final String databaseName;
    private final String tableName;
    private DataSource dataSource;

    public DatabaseHandlerBase(String host,
                               String port,
                               String databaseName,
                               String tableName,
                               String user,
                               String password,
                               boolean ssl){
        this.databaseName = databaseName;
        this.tableName = tableName;
        path = "jdbc:mysql://" + host + ":" + port + "/?" + "user=" + user + "&password=" + password;
        if (!ssl){
            path += "&useSSL=false";
        }
    }

    public DataSource getDataSource() throws SQLException {
        if (dataSource == null){
            SqlService service = Sponge.getServiceManager().provideUnchecked(SqlService.class);
            dataSource = service.getDataSource(path);
        }
        return dataSource;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getFullTableName(){
        return " " + getDatabaseName() + "." + getTableName() + " ";
    }

    private void createDatabase(){
        try (
                Connection connection = getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "create database if not exists " + getDatabaseName()
                )
                ){
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createTable(String sql){
        try (
                Connection connection = getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void query(Consumer<ResultSet> resultSetConsumer, String sql){
        try (
                Connection connection = getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ){
            ResultSet resultSet = statement.executeQuery();
            resultSetConsumer.accept(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
