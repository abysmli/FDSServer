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

public class SymptomDatabaseHandler {

    private Context context;
    private DataSource dataSource;
    private Connection connection;
    private Statement stmt;

    public void initConnections() throws NamingException, SQLException {
        this.context = new InitialContext();
        this.dataSource = (DataSource) context.lookup("java:comp/env/jdbc/FRS_SymptomKnowledge");
        this.connection = dataSource.getConnection();
        this.stmt = connection.createStatement();
    }

    private void releaseConnections() throws SQLException {
        this.stmt.close();
        this.connection.close();
    }

    public JSONArray getSymptomSubsystem() throws SQLException, NamingException {
        this.initConnections();
        ResultSet result = stmt.executeQuery("SELECT * FROM symptom_subsystem");
        JSONArray jsonarray = new JSONArray();
        while (result.next()) {
            JSONObject obj = new JSONObject();
            obj.put("symptom_id", result.getInt(1));
            obj.put("subsystem_id", result.getInt(2));
            obj.put("subsystem_name", result.getString(3));
            obj.put("component_id", result.getInt(4));
            obj.put("result", result.getString(5));
            jsonarray.put(obj);
        }
        result.close();
        this.releaseConnections();
        return jsonarray;
    }

    public JSONArray getSymptomAirpressure() throws SQLException, NamingException {
        this.initConnections();
        ResultSet result = stmt.executeQuery("SELECT * FROM symptom_airpressure_system");
        JSONArray jsonarray = new JSONArray();
        while (result.next()) {
            JSONObject obj = new JSONObject();
            obj.put("symptom_id", result.getInt(1));
            obj.put("component_id", result.getString(2));
            obj.put("component_name", result.getString(3));
            obj.put("parameter_type", result.getString(4));
            obj.put("parameter_value", result.getString(5));
            obj.put("parameter_value_oper", result.getString(6));
            obj.put("parameter_trend", result.getString(7));
            obj.put("parameter_changerate", result.getString(8));
            obj.put("parameter_changerate_oper", result.getString(9));
            obj.put("parameter_div", result.getString(10));
            obj.put("parameter2_type", result.getString(11));
            obj.put("parameter2_value", result.getString(12));
            obj.put("parameter2_value_oper", result.getString(13));
            obj.put("parameter2_trend", result.getString(14));
            obj.put("parameter2_changerate", result.getString(15));
            obj.put("parameter2_changerate_oper", result.getString(16));
            obj.put("parameter2_div", result.getString(17));
            obj.put("process", result.getInt(18));
            obj.put("result", result.getString(19));
            jsonarray.put(obj);
        }
        result.close();
        this.releaseConnections();
        return jsonarray;
    }

    public JSONArray getSymptomHeating() throws SQLException, NamingException {
        this.initConnections();
        ResultSet result = stmt.executeQuery("SELECT * FROM symptom_heating_system");
        JSONArray jsonarray = new JSONArray();
        while (result.next()) {
            JSONObject obj = new JSONObject();
            obj.put("symptom_id", result.getInt(1));
            obj.put("component_id", result.getString(2));
            obj.put("component_name", result.getString(3));
            obj.put("parameter_type", result.getString(4));
            obj.put("parameter_value", result.getString(5));
            obj.put("parameter_value_oper", result.getString(6));
            obj.put("parameter_trend", result.getString(7));
            obj.put("parameter_changerate", result.getString(8));
            obj.put("parameter_changerate_oper", result.getString(9));
            obj.put("parameter_div", result.getString(10));
            obj.put("parameter2_type", result.getString(11));
            obj.put("parameter2_value", result.getString(12));
            obj.put("parameter2_value_oper", result.getString(13));
            obj.put("parameter2_trend", result.getString(14));
            obj.put("parameter2_changerate", result.getString(15));
            obj.put("parameter2_changerate_oper", result.getString(16));
            obj.put("parameter2_div", result.getString(17));
            obj.put("process", result.getInt(18));
            obj.put("result", result.getString(19));
            jsonarray.put(obj);
        }
        result.close();
        this.releaseConnections();
        return jsonarray;
    }

    public JSONArray getSymptomInflow() throws SQLException, NamingException {
        this.initConnections();
        ResultSet result = stmt.executeQuery("SELECT * FROM symptom_inflow_system");
        JSONArray jsonarray = new JSONArray();
        while (result.next()) {
            JSONObject obj = new JSONObject();
            obj.put("symptom_id", result.getInt(1));
            obj.put("component_id", result.getString(2));
            obj.put("component_name", result.getString(3));
            obj.put("parameter_type", result.getString(4));
            obj.put("parameter_value", result.getString(5));
            obj.put("parameter_value_oper", result.getString(6));
            obj.put("parameter_trend", result.getString(7));
            obj.put("parameter_changerate", result.getString(8));
            obj.put("parameter_changerate_oper", result.getString(9));
            obj.put("parameter_div", result.getString(10));
            obj.put("min_sensor", result.getInt(11));
            obj.put("mid_sensor", result.getInt(12));
            obj.put("max_sensor", result.getInt(13));
            obj.put("process", result.getInt(14));
            obj.put("result", result.getString(15));
            jsonarray.put(obj);
        }
        result.close();
        this.releaseConnections();
        return jsonarray;
    }

    public JSONArray getSymptomPumping() throws SQLException, NamingException {
        this.initConnections();
        ResultSet result = stmt.executeQuery("SELECT * FROM symptom_pumping_system");
        JSONArray jsonarray = new JSONArray();
        while (result.next()) {
            JSONObject obj = new JSONObject();
            obj.put("symptom_id", result.getInt(1));
            obj.put("component_id", result.getString(2));
            obj.put("component_name", result.getString(3));
            obj.put("parameter_type", result.getString(4));
            obj.put("parameter_value", result.getString(5));
            obj.put("parameter_value_oper", result.getString(6));
            obj.put("parameter_trend", result.getString(7));
            obj.put("parameter_changerate", result.getString(8));
            obj.put("parameter_changerate_oper", result.getString(9));
            obj.put("parameter_div", result.getString(10));
            obj.put("parameter2_type", result.getString(11));
            obj.put("parameter2_value", result.getString(12));
            obj.put("parameter2_value_oper", result.getString(13));
            obj.put("parameter2_trend", result.getString(14));
            obj.put("parameter2_changerate", result.getString(15));
            obj.put("parameter2_changerate_oper", result.getString(16));
            obj.put("parameter2_div", result.getString(17));
            obj.put("prop_value", result.getString(16));
            obj.put("prop_value_oper", result.getString(17));
            obj.put("process", result.getInt(18));
            obj.put("result", result.getString(19));
            jsonarray.put(obj);
        }
        result.close();
        this.releaseConnections();
        return jsonarray;
    }
}
