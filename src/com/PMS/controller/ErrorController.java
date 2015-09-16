package com.PMS.controller;

import java.util.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.PMS.model.DatabaseHandler;

/**
 * @author Li, Yuan
 * Project: PMS
 */

public class ErrorController {
	
	private DatabaseHandler database = new DatabaseHandler();
	
	public JSONObject handleError(int mComponetID, String error_type, String error_desc) throws SQLException, NamingException {
		JSONObject ErrorObj = this.handleKnownError(mComponetID);
		if (ErrorObj.toString().equals("{}")) {
			ErrorObj = this.handleUnknownError(mComponetID, error_type, error_desc);
		}
		return ErrorObj;
	}
	
	private JSONObject handleKnownError(int mComponetID) throws SQLException, NamingException {
		return database.getErrorInfobyComponent(mComponetID);
	}

	public JSONObject handleUnknownError(int mComponentID, String error_type, String error_desc) throws SQLException, NamingException, JSONException{
		JSONObject mMainObj = new JSONObject();
		JSONObject mExecutionObj = new JSONObject();
		JSONArray mSubsystemIDs = getSubsystemIDbyComponent(mComponentID);
		JSONArray mFunctionIDs = getFunctionIDbyComponent(mComponentID);
		JSONArray mSubfunctionIDs = getSubfunctionIDbyFunction(mFunctionIDs);
		JSONArray mMainfunctionIDs = getMainfunctionIDbySubfunction(mSubfunctionIDs);
		mExecutionObj.put("subsystem_ids", mSubsystemIDs);
		mExecutionObj.put("function_ids", mFunctionIDs);
		mExecutionObj.put("subfunction_ids", mSubfunctionIDs);
		mExecutionObj.put("mainfunction_ids", mMainfunctionIDs);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		
		mMainObj.put("component_id", mComponentID);
		mMainObj.put("error_type", error_type);
		mMainObj.put("error_desc", error_desc);
		mMainObj.put("execute_command", mExecutionObj);
		mMainObj.put("insert_date", dateFormat.format(date));
		database.saveError(mMainObj);
		return mMainObj;
	}
	
	private JSONArray getMainfunctionIDbySubfunction(JSONArray mSubfunctionIDs) throws SQLException, NamingException {
		List<Integer> mMainfunctionID = database.getMainfunctionIDbySubfunction(mSubfunctionIDs);
		Iterator<Integer> mMainfunctionIterator = mMainfunctionID.iterator();
		JSONArray mMainfunctionArray = new JSONArray();
		
		while(mMainfunctionIterator.hasNext()) {
			mMainfunctionArray.put(mMainfunctionIterator.next());
		}
		return mMainfunctionArray;
	}

	private JSONArray getSubfunctionIDbyFunction(JSONArray mFunctionIDs) throws SQLException, NamingException {
		List<Integer> mSubfunctionID = database.getSubfunctionIDbyFunction(mFunctionIDs);
		Iterator<Integer> mSubfunctionIterator = mSubfunctionID.iterator();
		JSONArray mSubfunctionArray = new JSONArray();
		
		while(mSubfunctionIterator.hasNext()) {
			mSubfunctionArray.put(mSubfunctionIterator.next());
		}
		return mSubfunctionArray;
	}

	private JSONArray getFunctionIDbyComponent(int mComponentID) throws SQLException, NamingException {
		List<Integer> mFunctionsID = database.getFunctionIDbyComponent(mComponentID);
		Iterator<Integer> mFunctionsIterator = mFunctionsID.iterator();
		JSONArray mFunctionArray = new JSONArray();
		
		while(mFunctionsIterator.hasNext()) {
			mFunctionArray.put(mFunctionsIterator.next());
		}
		return mFunctionArray;
	}
	
	private JSONArray getSubsystemIDbyComponent(int mComponentID) throws SQLException, NamingException {
		List<Integer> mSubsystemID = database.getSubsystemIDbyComponent(mComponentID);
		Iterator<Integer> mSubsystemIterator = mSubsystemID.iterator();
		JSONArray mSubsystemArray = new JSONArray();
		
		while(mSubsystemIterator.hasNext()) {
			mSubsystemArray.put(mSubsystemIterator.next());
		}
		return mSubsystemArray;
	}

	public void updateStatus(JSONObject mResult) throws JSONException, NamingException, SQLException {
		database.updateComponents(mResult.getJSONArray("components"));
		database.updateFunctions(mResult.getJSONArray("functions"));
		database.updateSubsystems(mResult.getJSONArray("subsystems"));
		database.updateSubfunctions(mResult.getJSONArray("subfunctions"));
		database.updateMainfunctions(mResult.getJSONArray("mainfunctions"));		
	}
}
