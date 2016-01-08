package com.PMS.model;
/**
 * @author Li, Yuan
 * Project: PMS
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
		this.dataSource = (DataSource) context.lookup("java:comp/env/jdbc/PMS");
		this.connection = dataSource.getConnection();
		this.stmt = connection.createStatement();
	}

	private void releaseConnections() throws SQLException {
		this.stmt.close();
		this.connection.close();
	}

	public JSONArray getErrors() throws SQLException, NamingException {
		this.initConnections();
		ResultSet result = stmt.executeQuery("SELECT * FROM error_table");
		JSONArray jsonarray = new JSONArray();
		while (result.next()) {
			JSONObject obj = new JSONObject();
			obj.put("error_id", result.getInt(1));
			obj.put("component_id", result.getInt(2));
			obj.put("error_type", result.getString(3));
			obj.put("error_desc", result.getString(4));
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

	public JSONObject getErrorInfobyComponent(int mComponetID) throws SQLException, NamingException {
		this.initConnections();
		ResultSet result = stmt.executeQuery("SELECT * FROM error_table WHERE component_id = " + mComponetID);
		JSONObject errorobj = new JSONObject();
		while (result.next()) {
			errorobj.put("error_id", result.getInt(1));
			errorobj.put("component_id", result.getInt(2));
			errorobj.put("error_type", result.getString(3));
			errorobj.put("error_desc", result.getString(4));
			errorobj.put("execute_command", new JSONObject(result.getString(5)));
			errorobj.put("insert_date", result.getTimestamp(6));
		}
		result.close();
		this.releaseConnections();
		return errorobj;
	}

	public void saveError(JSONObject mMainObj) throws SQLException, NamingException {
		this.initConnections();
		stmt.executeUpdate(
				"INSERT INTO `PMS`.`error_table` (`error_id`, `component_id`, `error_type`, `error_desc`, `execute_command`, `insert_date`, `update_date`) VALUES (NULL, '"
						+ mMainObj.getInt("component_id") + "', '" + mMainObj.getString("error_type") + "', '"
						+ mMainObj.getString("error_desc") + "', '" + mMainObj.getJSONObject("execute_command")
						+ "', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
		this.releaseConnections();
	}

	public void updateComponents(JSONArray Components) throws NamingException, SQLException {
		this.initConnections();
		for (int i = 0; i < Components.length(); i++) {
			JSONObject obj = Components.getJSONObject(i);
			stmt.executeUpdate("UPDATE `PMS`.`component_table` SET `status` = '" + obj.getString("status")
					+ "' WHERE `component_table`.`component_id` = " + obj.getInt("component_id"));
		}
		this.releaseConnections();
	}

	public void updateFunctions(JSONArray Functions) throws NamingException, SQLException {
		this.initConnections();
		for (int i = 0; i < Functions.length(); i++) {
			JSONObject obj = Functions.getJSONObject(i);
			stmt.executeUpdate("UPDATE `PMS`.`function_table` SET `status` = '" + obj.getString("status")
					+ "' WHERE `function_table`.`function_id` = " + obj.getInt("function_id"));
		}
		this.releaseConnections();
	}

	public void updateSubsystems(JSONArray Subsystems) throws NamingException, SQLException {
		this.initConnections();
		for (int i = 0; i < Subsystems.length(); i++) {
			JSONObject obj = Subsystems.getJSONObject(i);
			stmt.executeUpdate("UPDATE `PMS`.`sub_system_table` SET `status` = '" + obj.getString("status")
					+ "' WHERE `sub_system_table`.`subsystem_id` = " + obj.getInt("subsystem_id"));
		}
		this.releaseConnections();
	}

	public void updateSubfunctions(JSONArray Subfunctions) throws NamingException, SQLException {
		this.initConnections();
		for (int i = 0; i < Subfunctions.length(); i++) {
			JSONObject obj = Subfunctions.getJSONObject(i);
			stmt.executeUpdate("UPDATE `PMS`.`sub_function_table` SET `status` = '" + obj.getString("status")
					+ "' WHERE `sub_function_table`.`subfunction_id` = " + obj.getInt("subfunction_id"));
		}
		this.releaseConnections();
	}

	public void updateMainfunctions(JSONArray Mainfunctions) throws NamingException, SQLException {
		this.initConnections();
		for (int i = 0; i < Mainfunctions.length(); i++) {
			JSONObject obj = Mainfunctions.getJSONObject(i);
			stmt.executeUpdate("UPDATE `PMS`.`main_function_table` SET `status` = '" + obj.getString("status")
					+ "' WHERE `main_function_table`.`mainfunction_id` = " + obj.getInt("mainfunction_id"));
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

	public JSONObject resetDatabase() throws SQLException, NamingException {
		this.initConnections();
		stmt.executeUpdate("UPDATE `PMS`.`component_table` SET `status` = 'active'");
		stmt.executeUpdate("UPDATE `PMS`.`sub_system_table` SET `status` = 'active'");
		stmt.executeUpdate("UPDATE `PMS`.`function_table` SET `status` = 'active'");
		stmt.executeUpdate("UPDATE `PMS`.`sub_function_table` SET `status` = 'active'");
		stmt.executeUpdate("UPDATE `PMS`.`main_function_table` SET `status` = 'active'");
		stmt.executeUpdate("TRUNCATE `PMS`.`error_table`");
		this.releaseConnections();
		JSONObject obj = new JSONObject();
		obj.put("result", "success");
		return obj;
	}

	public void updateComponentValue(JSONObject mResult) throws SQLException, NamingException {
		this.initConnections();
		stmt.executeUpdate("INSERT INTO `PMS`.`component_value_buffer_table` (meta_data, process_id, timestamp) VALUES ('"+ mResult.getJSONArray("components").toString() +"', "+ mResult.getInt("process_id") +", "+ mResult.getString("stamp_time")+")");
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
}
