package com.ruskaof.server.data.remote.posturesql;

import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Class for holding database tables for working with them
 */
public class Database {
    private final UsersTable usersTable;
    private final StudyGroupTable studyGroupTable;
    private final Logger logger;

    public Database(Connection connection, Logger logger) {
        this.studyGroupTable = new StudyGroupTable(connection);
        this.usersTable = new UsersTable(connection);
        this.logger = logger;

        try {
            initTables();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initTables() throws SQLException {
        studyGroupTable.init();
        usersTable.init();
    }

    public StudyGroupTable getStudyGroupTable() {
        return studyGroupTable;
    }

    public UsersTable getUsersTable() {
        return usersTable;
    }
}
