package com.ck.ProjectManager.database.handlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by cndet on 16/03/2017.
 */
public class MasterHandler {
    protected static Connection c;

    protected static void startConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:test.db");
    }

    protected static void startConnection(String createCheck) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:test.db");
        c.createStatement().execute(createCheck);
    }

}
