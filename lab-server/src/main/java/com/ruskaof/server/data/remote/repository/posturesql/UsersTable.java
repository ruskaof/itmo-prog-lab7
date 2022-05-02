package com.ruskaof.server.data.remote.repository.posturesql;

import com.ruskaof.common.data.User;
import com.ruskaof.server.util.Encryptor;

import java.sql.*;
import java.util.TreeSet;

public class UsersTable extends Table<User> {
    private final Connection connection;

    public UsersTable(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void init() throws SQLException {
        try (
                Statement statement = connection.createStatement()
        ) {
            statement.execute("CREATE TABLE IF NOT EXISTS users ("
                    + "    id serial PRIMARY KEY,"
                    + "    login varchar(50) NOT NULL UNIQUE,"
                    + "    password varchar(70) NOT NULL)");
        }
    }


    @Override
    public User mapRowToObject(ResultSet resultSet) throws SQLException {

        return new User(
                resultSet.getLong("id"),
                resultSet.getString("password"),
                resultSet.getString("login")
        );

    }

    @Override
    public long add(User element) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO users VALUES (default, ?, ?) RETURNING id"
        )) {
            makePreparedStatement(preparedStatement, element);
            try (
                    ResultSet resultSet = preparedStatement.executeQuery()
            ) {
                resultSet.next();
                return resultSet.getLong("id");
            }
        }
    }


    @Override
    public TreeSet<User> getCollection() throws SQLException {
        final TreeSet<User> newCollection = new TreeSet<>();

        try (
                Statement statement = connection.createStatement()
        ) {
            try (
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM users")
            ) {

                while (resultSet.next()) {
                    User user = mapRowToObject(resultSet);
                    newCollection.add(user);
                }
            }
        }

        return newCollection;
    }

    private void makePreparedStatement(PreparedStatement preparedStatement, User user) throws SQLException {
        int currentParameterOffset = 0;
        preparedStatement.setString(++currentParameterOffset, user.getName());
        preparedStatement.setString(++currentParameterOffset, Encryptor.encryptThisString(user.getPassword()));
    }
}
