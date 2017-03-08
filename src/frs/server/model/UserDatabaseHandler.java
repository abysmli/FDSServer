/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.server.model;

/**
 * @author Li, Yuan Project: FRSServer
 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserDatabaseHandler {

    private Context context;
    private DataSource dataSource;
    private Connection connection;
    private Statement stmt;

    public void initConnections() throws NamingException, SQLException {
        this.context = new InitialContext();
        this.dataSource = (DataSource) context.lookup("java:comp/env/jdbc/FRS_SystemKnowledge");
        this.connection = dataSource.getConnection();
        this.stmt = connection.createStatement();
    }

    private void releaseConnections() throws SQLException {
        this.stmt.close();
        this.connection.close();
    }

    public JSONObject getUser(String username) throws NamingException, SQLException {
        this.initConnections();
        ResultSet result = stmt.executeQuery("SELECT * FROM `user_table` WHERE `username` = '" + username + "'");
        JSONObject obj = new JSONObject();
        while (result.next()) {
            obj.put("userid", result.getInt(1));
            obj.put("username", result.getString(2));
            obj.put("password", result.getString(3));
            obj.put("email", result.getString(4));
            obj.put("level", result.getString(5));
            obj.put("insert_date", result.getTimestamp(6));
        }
        result.close();
        this.releaseConnections();
        return obj;
    }
}
