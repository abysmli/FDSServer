package frs.server.controller;

import frs.server.model.FaultDatabaseHandler;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import frs.server.model.SymptomDatabaseHandler;
import frs.server.model.SystemDatabaseHandler;
import frs.server.model.UserDatabaseHandler;

public class UserController {

    private final SymptomDatabaseHandler databaseSymptom = new SymptomDatabaseHandler();
    private final FaultDatabaseHandler databaseFault = new FaultDatabaseHandler();
    private final SystemDatabaseHandler databaseSystem = new SystemDatabaseHandler();
    private final UserDatabaseHandler databaseUser = new UserDatabaseHandler();

    public JSONObject getUser(String username, String password) throws NamingException, SQLException, JSONException {
        JSONObject obj = databaseUser.getUser(username);
        if (obj.getString("password").equals(password)) {
            obj.put("status", "success");
        } else {
            obj.put("status", "wrong");
        }
        return obj;
    }

    // TODO: implements later
    public int delUser(String username) {
        return 0;

    }

    // TODO: implements later
    public int addUser(JSONObject user) {
        return 0;

    }

    // TODO: implements later
    public int updateUser(String username) {
        return 0;
    }

    // TODO: implements later
    public JSONArray getAllUsers() {
        return null;

    }
}
