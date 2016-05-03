package com.fds.server.model;

/**
 * @author Li, Yuan Project: FDSServer
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

public class DatabaseHandler {

    private Context context;
    private DataSource dataSource;
    private Connection connection;
    private Statement stmt;

    public void initConnections() throws NamingException, SQLException {
        this.context = new InitialContext();
        this.dataSource = (DataSource) context.lookup("java:comp/env/jdbc/FDS");
        this.connection = dataSource.getConnection();
        this.stmt = connection.createStatement();
    }

    private void releaseConnections() throws SQLException {
        this.stmt.close();
        this.connection.close();
    }

    public JSONArray getFaults() throws SQLException, NamingException {
        this.initConnections();
        ResultSet result = stmt.executeQuery("SELECT * FROM fault_table");
        JSONArray jsonarray = new JSONArray();
        while (result.next()) {
            JSONObject obj = new JSONObject();
            obj.put("fault_id", result.getInt(1));
            obj.put("component_id", result.getInt(2));
            obj.put("fault_type", result.getString(3));
            obj.put("fault_desc", result.getString(4));
            obj.put("execute_command", result.getString(5));
            obj.put("insert_date", result.getTimestamp(6));
            jsonarray.put(obj);
        }
        result.close();
        this.releaseConnections();
        return jsonarray;
    }

    public JSONArray getSymptoms() throws SQLException, NamingException {
        this.initConnections();
        ResultSet result = stmt.executeQuery("SELECT * FROM symptom_table");
        JSONArray jsonarray = new JSONArray();
        while (result.next()) {
            JSONObject obj = new JSONObject();
            obj.put("symptom_id", result.getInt(1));
            obj.put("component_id", result.getInt(2));
            obj.put("description", result.getString(3));
            obj.put("min_limit", result.getDouble(4));
            obj.put("max_limit", result.getDouble(5));
            jsonarray.put(obj);
        }
        result.close();
        this.releaseConnections();
        return jsonarray;
    }

    public JSONArray getComponents() throws SQLException, NamingException {
        this.initConnections();
        ResultSet result = stmt.executeQuery("SELECT * FROM component_table");
        JSONArray jsonarray = new JSONArray();
        while (result.next()) {
            JSONObject obj = new JSONObject();
            obj.put("component_id", result.getInt(1));
            obj.put("component_name", result.getString(2));
            obj.put("series", result.getString(3));
            obj.put("type", result.getString(4));
            obj.put("component_symbol", result.getString(5));
            obj.put("component_desc", result.getString(6));
            obj.put("activition", result.getString(7));
            obj.put("status", result.getString(8));
            obj.put("insert_date", result.getTimestamp(9));
            jsonarray.put(obj);
        }
        result.close();
        this.releaseConnections();
        return jsonarray;
    }

    public JSONArray getSubsystems() throws SQLException, NamingException {
        this.initConnections();
        ResultSet result = stmt.executeQuery("SELECT * FROM sub_system_table");
        JSONArray jsonarray = new JSONArray();
        while (result.next()) {
            JSONObject obj = new JSONObject();
            obj.put("subsystem_id", result.getInt(1));
            obj.put("subsystem_desc", result.getString(2));
            obj.put("status", result.getString(3));
            obj.put("insert_date", result.getTimestamp(4));
            jsonarray.put(obj);
        }
        result.close();
        this.releaseConnections();
        return jsonarray;
    }

    public JSONArray getFunctions() throws SQLException, NamingException {
        this.initConnections();
        ResultSet result = stmt.executeQuery("SELECT * FROM function_table");
        JSONArray jsonarray = new JSONArray();
        while (result.next()) {
            JSONObject obj = new JSONObject();
            obj.put("function_id", result.getInt(1));
            obj.put("function_desc", result.getString(2));
            obj.put("status", result.getString(3));
            obj.put("insert_date", result.getTimestamp(4));
            jsonarray.put(obj);
        }
        result.close();
        this.releaseConnections();
        return jsonarray;
    }

    public JSONArray getSubsystemComponentRel() throws SQLException, NamingException {
        this.initConnections();
        ResultSet result = stmt.executeQuery("SELECT * FROM subsystem_component_rel");
        JSONArray jsonarray = new JSONArray();
        while (result.next()) {
            JSONObject obj = new JSONObject();
            obj.put("subsystem_id", result.getInt(1));
            obj.put("component_id", result.getInt(2));
            jsonarray.put(obj);
        }
        result.close();
        this.releaseConnections();
        return jsonarray;
    }

    public JSONArray getComponentFunctionRel() throws SQLException, NamingException {
        this.initConnections();
        ResultSet result = stmt.executeQuery("SELECT * FROM component_function_rel");
        JSONArray jsonarray = new JSONArray();
        while (result.next()) {
            JSONObject obj = new JSONObject();
            obj.put("component_id", result.getInt(1));
            obj.put("function_id", result.getInt(2));
            jsonarray.put(obj);
        }
        result.close();
        this.releaseConnections();
        return jsonarray;
    }

    public JSONArray getSubfunctionFunctionRel() throws SQLException, NamingException {
        this.initConnections();
        ResultSet result = stmt.executeQuery("SELECT * FROM subfunction_function_rel");
        JSONArray jsonarray = new JSONArray();
        while (result.next()) {
            JSONObject obj = new JSONObject();
            obj.put("subfunction_id", result.getInt(1));
            obj.put("function_id", result.getInt(2));
            jsonarray.put(obj);
        }
        result.close();
        this.releaseConnections();
        return jsonarray;
    }

    public JSONArray getMainfunctionSubfunctionRel() throws SQLException, NamingException {
        this.initConnections();
        ResultSet result = stmt.executeQuery("SELECT * FROM mainfunction_subfunction_rel");
        JSONArray jsonarray = new JSONArray();
        while (result.next()) {
            JSONObject obj = new JSONObject();
            obj.put("mainfunction_id", result.getInt(1));
            obj.put("subfunction_id", result.getInt(2));
            jsonarray.put(obj);
        }
        result.close();
        this.releaseConnections();
        return jsonarray;
    }

    public JSONArray getSubfunctions() throws SQLException, NamingException {
        this.initConnections();
        ResultSet result = stmt.executeQuery("SELECT * FROM sub_function_table");
        JSONArray jsonarray = new JSONArray();
        while (result.next()) {
            JSONObject obj = new JSONObject();
            obj.put("subfunction_id", result.getInt(1));
            obj.put("subfunction_desc", result.getString(2));
            obj.put("status", result.getString(3));
            obj.put("insert_date", result.getTimestamp(4));
            jsonarray.put(obj);
        }
        result.close();
        this.releaseConnections();
        return jsonarray;
    }

    public JSONArray getMainfunctions() throws SQLException, NamingException {
        this.initConnections();
        ResultSet result = stmt.executeQuery("SELECT * FROM main_function_table");
        JSONArray jsonarray = new JSONArray();
        while (result.next()) {
            JSONObject obj = new JSONObject();
            obj.put("mainfunction_id", result.getInt(1));
            obj.put("mainfunction_desc", result.getString(2));
            obj.put("status", result.getString(3));
            obj.put("insert_date", result.getTimestamp(4));
            jsonarray.put(obj);
        }
        result.close();
        this.releaseConnections();
        return jsonarray;
    }

    public List<Integer> getFunctionIDbyComponent(int mComponentID) throws SQLException, NamingException {
        this.initConnections();
        ResultSet result = stmt.executeQuery(
                "SELECT component_function_rel.function_id FROM component_table INNER JOIN component_function_rel ON component_table.component_id = component_function_rel.component_id where component_table.component_id = "
                + mComponentID);
        List<Integer> mFunctionIDs = new ArrayList<Integer>();
        while (result.next()) {
            mFunctionIDs.add(result.getInt(1));
        }
        result.close();
        this.releaseConnections();
        return mFunctionIDs;
    }

    public List<Integer> getSubsystemIDbyComponent(int mComponentID) throws SQLException, NamingException {
        this.initConnections();
        ResultSet result = stmt.executeQuery(
                "SELECT subsystem_component_rel.subsystem_id FROM component_table INNER JOIN subsystem_component_rel ON component_table.component_id = subsystem_component_rel.component_id where component_table.component_id = "
                + mComponentID);
        List<Integer> mSubsystemIDs = new ArrayList<Integer>();
        while (result.next()) {
            mSubsystemIDs.add(result.getInt(1));
        }
        result.close();
        this.releaseConnections();
        return mSubsystemIDs;
    }

    public List<Integer> getSubfunctionIDbyFunction(JSONArray mFunctionIDs) throws SQLException, NamingException {
        this.initConnections();
        String mSubquery = "";
        for (int i = 0; i < mFunctionIDs.length(); i++) {
            if (mFunctionIDs.length() - 1 == i) {
                mSubquery += "function_table.function_id = " + mFunctionIDs.getInt(i);
            } else {
                mSubquery += "function_table.function_id = " + mFunctionIDs.getInt(i) + " OR ";
            }
        }
        ResultSet result = stmt.executeQuery(
                "SELECT subfunction_function_rel.subfunction_id FROM function_table INNER JOIN subfunction_function_rel ON function_table.function_id = subfunction_function_rel.function_id WHERE "
                + mSubquery + " GROUP BY subfunction_function_rel.subfunction_id");
        List<Integer> SubfunctionIDs = new ArrayList<Integer>();
        while (result.next()) {
            SubfunctionIDs.add(result.getInt(1));
        }
        result.close();
        this.releaseConnections();
        return SubfunctionIDs;
    }

    public List<Integer> getMainfunctionIDbySubfunction(JSONArray mSubfunctionIDs)
            throws SQLException, NamingException {
        this.initConnections();
        String mSubquery = "";
        for (int i = 0; i < mSubfunctionIDs.length(); i++) {
            if (mSubfunctionIDs.length() - 1 == i) {
                mSubquery += "mainfunction_subfunction_rel.subfunction_id = " + mSubfunctionIDs.getInt(i);
            } else {
                mSubquery += "mainfunction_subfunction_rel.subfunction_id = " + mSubfunctionIDs.getInt(i) + " OR ";
            }
        }
        ResultSet result = stmt.executeQuery(
                "SELECT mainfunction_subfunction_rel.mainfunction_id FROM mainfunction_subfunction_rel WHERE "
                + mSubquery + " GROUP BY mainfunction_subfunction_rel.mainfunction_id");
        List<Integer> MainfunctionIDs = new ArrayList<Integer>();
        while (result.next()) {
            MainfunctionIDs.add(result.getInt(1));
        }
        result.close();
        this.releaseConnections();
        return MainfunctionIDs;
    }

    public JSONObject getFaultInfobyComponent(int mComponetID) throws SQLException, NamingException {
        this.initConnections();
        ResultSet result = stmt.executeQuery("SELECT * FROM fault_table WHERE component_id = " + mComponetID);
        JSONObject faultobj = new JSONObject();
        while (result.next()) {
            faultobj.put("fault_id", result.getInt(1));
            faultobj.put("component_id", result.getInt(2));
            faultobj.put("fault_type", result.getString(3));
            faultobj.put("fault_desc", result.getString(4));
            faultobj.put("execute_command", new JSONObject(result.getString(5)));
            faultobj.put("insert_date", result.getTimestamp(6));
        }
        result.close();
        this.releaseConnections();
        return faultobj;
    }

    public void saveFault(JSONObject mMainObj) throws SQLException, NamingException {
        this.initConnections();
        stmt.executeUpdate(
                "INSERT INTO `FDS`.`fault_table` (`fault_id`, `component_id`, `fault_type`, `fault_desc`, `execute_command`, `insert_date`, `update_date`) VALUES (NULL, '"
                + mMainObj.getInt("component_id") + "', '" + mMainObj.getString("fault_type") + "', '"
                + mMainObj.getString("fault_desc") + "', '" + mMainObj.getJSONObject("execute_command")
                + "', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
        this.releaseConnections();
    }

    public void updateComponents(int componentId) throws NamingException, SQLException {
        this.initConnections();
        stmt.executeUpdate(
                "UPDATE `FDS`.`component_table` SET `status` = 'inactive' WHERE `component_table`.`component_id` = "
                + componentId);
        this.releaseConnections();
    }

    public void updateFunctions(JSONArray Functions) throws NamingException, SQLException {
        this.initConnections();
        for (int i = 0; i < Functions.length(); i++) {
            stmt.executeUpdate(
                    "UPDATE `FDS`.`function_table` SET `status` = 'inactive' WHERE `function_table`.`function_id` = "
                    + Functions.getInt(i));
        }
        this.releaseConnections();
    }

    public void updateSubsystems(JSONArray Subsystems) throws NamingException, SQLException {
        this.initConnections();
        for (int i = 0; i < Subsystems.length(); i++) {
            stmt.executeUpdate(
                    "UPDATE `FDS`.`sub_system_table` SET `status` = 'inactive' WHERE `sub_system_table`.`subsystem_id` = "
                    + Subsystems.getInt(i));
        }
        this.releaseConnections();
    }

    public void updateSubfunctions(JSONArray Subfunctions) throws NamingException, SQLException {
        this.initConnections();
        for (int i = 0; i < Subfunctions.length(); i++) {
            stmt.executeUpdate(
                    "UPDATE `FDS`.`sub_function_table` SET `status` = 'inactive' WHERE `sub_function_table`.`subfunction_id` = "
                    + Subfunctions.getInt(i));
        }
        this.releaseConnections();
    }

    public void updateMainfunctions(JSONArray Mainfunctions) throws NamingException, SQLException {
        this.initConnections();
        for (int i = 0; i < Mainfunctions.length(); i++) {
            stmt.executeUpdate(
                    "UPDATE `FDS`.`main_function_table` SET `status` = 'inactive' WHERE `main_function_table`.`mainfunction_id` = "
                    + Mainfunctions.getInt(i));
        }
        this.releaseConnections();
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

    public void updateComponentValue(JSONObject mResult) throws SQLException, NamingException {
        this.initConnections();
        stmt.executeUpdate(
                "INSERT INTO `FDS`.`component_value_buffer_table` (meta_data, process_id, timestamp) VALUES ('" + mResult.getJSONArray("components").toString() + "', " + mResult.getInt("process_id") + ", "
                + mResult.getString("stamp_time") + ")");
        this.releaseConnections();
    }

    public JSONObject getLastComponentValue() throws NamingException, SQLException {
        this.initConnections();
        ResultSet result = stmt.executeQuery("SELECT * FROM `component_value_buffer_table` ORDER BY id DESC LIMIT 1");
        JSONObject obj = new JSONObject();
        while (result.next()) {
            obj.put("id", result.getLong(1));
            obj.put("components", new JSONArray(result.getString(2)));
            obj.put("process_id", result.getInt(3));
            obj.put("stamp_time", result.getString(4));
        }
        result.close();
        this.releaseConnections();
        return obj;
    }

    public JSONArray getComponentValue() throws NamingException, SQLException {
        this.initConnections();
        ResultSet result = stmt
                .executeQuery("SELECT * FROM `component_value_buffer_table` ORDER BY id DESC LIMIT 7200");
        JSONArray jsonarray = new JSONArray();
        while (result.next()) {
            JSONObject obj = new JSONObject();
            obj.put("id", result.getLong(1));
            obj.put("components", new JSONArray(result.getString(2)));
            obj.put("process_id", result.getInt(3));
            obj.put("stamp_time", result.getString(4));
            jsonarray.put(obj);
        }
        result.close();
        this.releaseConnections();
        return jsonarray;
    }

    public void saveFaultDiagnoseProcedure(int componetID, String series, String faultType, String faultDesc,
            String diagnoseProcedureInfo, String solution) throws NamingException, SQLException {
        this.initConnections();
        stmt.executeUpdate(
                "INSERT INTO `FDS`.`fault_diagnose_buffer_table` (diagnose_procedure, fault_desc, fault_type, component_id, series, solution) VALUES ('"
                + diagnoseProcedureInfo + "', '" + faultDesc + "', '" + faultType + "', " + componetID + ", '"
                + series + "', '" + solution + "')");
        this.releaseConnections();
    }

    public JSONArray getFaultProcedureInfos() throws NamingException, SQLException {
        this.initConnections();
        ResultSet result = stmt.executeQuery("SELECT * FROM fault_diagnose_buffer_table");
        JSONArray jsonarray = new JSONArray();
        while (result.next()) {
            JSONObject obj = new JSONObject();
            obj.put("fault_id", result.getInt(1));
            obj.put("diagnose_procedure", result.getString(2));
            obj.put("fault_desc", result.getString(3));
            obj.put("fault_type", result.getString(4));
            obj.put("component_id", result.getInt(5));
            obj.put("series", result.getString(6));
            obj.put("solution", result.getString(7));
            obj.put("insert_date", result.getTimestamp(8));
            jsonarray.put(obj);
        }
        result.close();
        this.releaseConnections();
        return jsonarray;
    }

    public JSONObject resetDatabase() throws SQLException, NamingException {
        this.initConnections();
        stmt.executeUpdate("UPDATE `FDS`.`component_table` SET `status` = 'active'");
        stmt.executeUpdate("UPDATE `FDS`.`sub_system_table` SET `status` = 'active'");
        stmt.executeUpdate("UPDATE `FDS`.`function_table` SET `status` = 'active'");
        stmt.executeUpdate("UPDATE `FDS`.`sub_function_table` SET `status` = 'active'");
        stmt.executeUpdate("UPDATE `FDS`.`main_function_table` SET `status` = 'active'");
        stmt.executeUpdate("UPDATE `FDS`.`requirement_table` SET `status` = 'active'");
        stmt.executeUpdate("TRUNCATE `FDS`.`fault_table`");
        // stmt.executeUpdate("TRUNCATE `FDS`.`component_value_buffer_table`");
        stmt.executeUpdate("TRUNCATE `FDS`.`fault_diagnose_buffer_table`");
        this.releaseConnections();
        JSONObject obj = new JSONObject();
        obj.put("result", "success");
        return obj;
    }
}
