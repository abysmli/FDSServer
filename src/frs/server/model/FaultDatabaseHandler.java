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
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONObject;

public class FaultDatabaseHandler {

    private Context context;
    private DataSource dataSource;
    private Connection connection;
    private Statement stmt;

    public void initConnections() throws NamingException, SQLException {
        this.context = new InitialContext();
        this.dataSource = (DataSource) context.lookup("java:comp/env/jdbc/FRS_FaultKnowledge");
        this.connection = dataSource.getConnection();
        this.stmt = connection.createStatement();
    }

    private void releaseConnections() throws SQLException {
        this.stmt.close();
        this.connection.close();
    }

    public JSONArray getFaultKnowledge() throws SQLException, NamingException {
        this.initConnections();
        ResultSet result = stmt.executeQuery("SELECT * FROM fault_knowledge");
        JSONArray jsonarray = new JSONArray();
        while (result.next()) {
            JSONObject obj = new JSONObject();
            obj.put("fault_id", result.getInt(1));
            obj.put("fault_no", result.getInt(2));
            obj.put("fault_name", result.getString(3));
            obj.put("symptom_id", result.getInt(4));
            obj.put("symptom_desc", result.getString(5));
            obj.put("available_functions", result.getString(6));
            obj.put("reconf_command", result.getString(7));
            obj.put("fault_parameter", result.getString(8));
            obj.put("fault_value", result.getString(9));
            obj.put("fault_effect", result.getString(10));
            obj.put("fault_location", result.getString(11));
            obj.put("fault_message", result.getString(12));
            obj.put("check_status", result.getString(13));
            obj.put("equipment_id", result.getString(14));
            obj.put("occured_at", result.getTimestamp(15));
            obj.put("update_at", result.getTimestamp(16));
            jsonarray.put(obj);
        }
        result.close();
        this.releaseConnections();
        return jsonarray;
    }

    public void saveFaultKnowledge(JSONObject mMainObj) throws SQLException, NamingException {
        this.initConnections();
        stmt.executeUpdate(
                "INSERT INTO `fault_knowledge` (`fault_no`, `fault_name`, `symptom_id`, `symptom_desc`, `available_functions`, `reconf_command`, `fault_parameter`, `fault_value`, `fault_effect`, `fault_location`, `fault_message`, `check_status`, `equipment_id`, `occured_at`) VALUES ('"
                + mMainObj.getInt("fault_no") + "', '"
                + mMainObj.getString("fault_name") + "', '"
                + mMainObj.getInt("symptom_id") + "', '"
                + mMainObj.getString("symptom_desc") + "', '"
                + mMainObj.getJSONObject("available_functions").toString() + "', '"
                + mMainObj.getJSONObject("reconf_command").toString() + "', '"
                + mMainObj.getString("fault_parameter") + "', '"
                + mMainObj.getString("fault_value") + "', '"
                + mMainObj.getString("fault_effect") + "', '"
                + mMainObj.getString("fault_location") + "', '"
                + mMainObj.getString("fault_message") + "', '"
                + mMainObj.getString("check_status") + "', '"
                + mMainObj.getString("equipment_id")
                + "', CURRENT_TIMESTAMP)");
        this.releaseConnections();
    }

    public JSONArray getFaultStatistic() throws SQLException, NamingException {
        this.initConnections();
        ResultSet result = stmt.executeQuery("SELECT * FROM fault_statistic");
        JSONArray jsonarray = new JSONArray();
        while (result.next()) {
            JSONObject obj = new JSONObject();
            obj.put("fault_id", result.getInt(1));
            obj.put("occured_sum", result.getInt(2));
            obj.put("fault_location", result.getString(3));
            obj.put("parameter_type", result.getString(4));
            obj.put("parameter_value", result.getString(5));
            obj.put("parameter_div", result.getString(6));
            obj.put("parameter_trend", result.getString(7));
            obj.put("parameter_changerate", result.getString(8));
            obj.put("update_at", result.getTimestamp(9));
            jsonarray.put(obj);
        }
        result.close();
        this.releaseConnections();
        return jsonarray;
    }

    public void saveFaultStatistic(JSONObject mMainObj) throws SQLException, NamingException {
        this.initConnections();
        stmt.executeUpdate(
                "INSERT INTO `fault_statistic` (`fault_id`, `occured_sum`, `fault_location`, `parameter_type`, `parameter_value`, `parameter_div`, `parameter_trend`, `parameter_changerate`) VALUES (NULL, '"
                + mMainObj.getInt("occured_sum") + "', '"
                + mMainObj.getInt("fault_location") + "', '"
                + mMainObj.getString("parameter_type") + "', '"
                + mMainObj.getString("parameter_value") + "', '"
                + mMainObj.getString("parameter_div") + "', '"
                + mMainObj.getString("parameter_trend") + "', '"
                + mMainObj.getString("parameter_changerate") + "', '"
                + "', CURRENT_TIMESTAMP)");
        this.releaseConnections();
    }
    
    public JSONObject getFaultInfobyComponent(int mComponetID) throws SQLException, NamingException {
        this.initConnections();
        ResultSet result = stmt.executeQuery("SELECT * FROM fault_knowledge WHERE fault_location = " + mComponetID);
        JSONObject obj = new JSONObject();
        while (result.next()) {            
            obj.put("fault_id", result.getInt(1));
            obj.put("fault_no", result.getInt(2));
            obj.put("fault_name", result.getString(3));
            obj.put("symptom_id", result.getInt(4));
            obj.put("symptom_desc", result.getString(5));
            obj.put("available_functions", result.getString(6));
            obj.put("reconf_command", result.getString(7));
            obj.put("fault_effect", result.getString(8));
            obj.put("fault_location", result.getString(9));
            obj.put("fault_message", result.getString(10));
            obj.put("check_status", result.getString(11));
            obj.put("equipment_id", result.getString(12));
            obj.put("occured_at", result.getTimestamp(13));
            obj.put("update_at", result.getTimestamp(14));
        }
        result.close();
        this.releaseConnections();
        return obj;
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

    public void saveFaultDiagnoseProcedure(int componetID, String series, String faultType, String faultDesc,
            String diagnoseProcedureInfo, String solution) throws NamingException, SQLException {
        this.initConnections();
        stmt.executeUpdate(
                "INSERT INTO `fault_diagnose_buffer_table` (diagnose_procedure, fault_desc, fault_type, component_id, series, solution) VALUES ('"
                + diagnoseProcedureInfo + "', '" + faultDesc + "', '" + faultType + "', " + componetID + ", '"
                + series + "', '" + solution + "')");
        this.releaseConnections();
    }
}
