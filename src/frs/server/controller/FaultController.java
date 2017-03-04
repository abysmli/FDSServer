package frs.server.controller;

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

import frs.server.model.SymptomDatabaseHandler;

/**
 * @author Li, Yuan Project: FDSServer
 */

public class FaultController {

	private final SymptomDatabaseHandler database = new SymptomDatabaseHandler();
	int step = 1;
	JSONArray DiagnoseProcedureInfo = new JSONArray();

	public JSONObject handleFault(int mComponetID, String mSeries, String fault_type, String fault_desc)
			throws SQLException, NamingException {
		JSONObject FaultObj = new JSONObject();
		generateFaultDiagnoseInfo(step++, mSeries, "Scanning Abfuellanlage...", "A fault is found");
            switch (fault_type) {
                case "known":
                    FaultObj = handleKnownFault(mComponetID, mSeries);
                    if (!FaultObj.toString().equals("{}")) {
                        generateFaultDiagnoseInfo(step++, mSeries, "Check wether the fault is known ...",
                                "This fault is marked as known.");
                        generateFaultDiagnoseInfo(step++, mSeries, "Loading the reconfiguration solutions from Database ...",
                                "Solution -> Deactive Mainfunction " + FaultObj.getJSONObject("execute_command").getJSONArray("mainfunction_ids")
                                        .toString());
                    } else {
                        generateFaultDiagnoseInfo(step++, mSeries, "Check wether the fault is known ...",
                                "This fault is marked as known in Anlage, but not be recorded in database! Now the solution will be regenerated! ");
                        FaultObj = handleUnknownFault(mComponetID, mSeries, fault_type, fault_desc);
                        generateFaultDiagnoseInfo(step++, mSeries, "Generating reconfiguration solutions ...",
                                "Solution -> Deactive Mainfunction " + FaultObj.getJSONObject("execute_command")
                                        .getJSONArray("mainfunction_ids").toString());
                    }
                    break;
                case "unknown":
                    generateFaultDiagnoseInfo(step++, mSeries, "Check wether the fault is known ...",
                            "This fault is marked as unknown.");
                    FaultObj = handleKnownFault(mComponetID, mSeries);
                    if (!FaultObj.toString().equals("{}")) {
                        generateFaultDiagnoseInfo(step++, mSeries,
                                "Scanning Database to check wether the fault was solved before ...",
                                "This fault has already been solved before.");
                        generateFaultDiagnoseInfo(step++, mSeries, "Loading the reconfiguration solutions from Database ...",
                                "Solution -> Deactive Mainfunction " + FaultObj.getJSONObject("execute_command").getJSONArray("mainfunction_ids")
                                        .toString());
                    } else {
                        generateFaultDiagnoseInfo(step++, mSeries,
                                "Scanning Database to check wether the fault was solved before ...",
                                "This fault was never happend before.");
                        FaultObj = handleUnknownFault(mComponetID, mSeries, fault_type, fault_desc);
                        generateFaultDiagnoseInfo(step++, mSeries, "Generating reconfiguration solutions ...",
                                "Solution -> Deactive Mainfunction " + FaultObj.getJSONObject("execute_command")
                                        .getJSONArray("mainfunction_ids").toString());
                    }
                    break;
                case "defect":
                    generateFaultDiagnoseInfo(step++, mSeries, "Check wether the fault is known ...",
                            "This fault is marked as defect.");
                    FaultObj = handleKnownFault(mComponetID, mSeries);
                    if (!FaultObj.toString().equals("{}")) {
                        generateFaultDiagnoseInfo(step++, mSeries,
                                "Scanning Database to check wether the fault was solved before ...",
                                "This fault has already been solved before.");
                        generateFaultDiagnoseInfo(step++, mSeries, "Loading the reconfiguration solutions from Database ...",
                                "Solution -> Deactive Mainfunction " + FaultObj.getJSONObject("execute_command").getJSONArray("mainfunction_ids")
                                        .toString());
                    } else {
                        generateFaultDiagnoseInfo(step++, mSeries,
                                "Scanning Database to check wether the fault was solved before ...",
                                "This fault was never happend before.");
                        FaultObj = handleUnknownFault(mComponetID, mSeries, fault_type, fault_desc);
                        generateFaultDiagnoseInfo(step++, mSeries, "Generating reconfiguration solutions ...",
                                "Solution -> Deactive Mainfunction " + FaultObj.getJSONObject("execute_command")
                                        .getJSONArray("mainfunction_ids").toString());
                    }
                    break;
                case "shift":
                    generateFaultDiagnoseInfo(step++, mSeries, "Check wether the fault is known ...",
                            "This fault is marked as shift.");
                    FaultObj = handleKnownFault(mComponetID, mSeries);
                    if (!FaultObj.toString().equals("{}")) {
                        generateFaultDiagnoseInfo(step++, mSeries,
                                "Scanning Database to check wether the fault was solved before ...",
                                "This fault has already been solved before.");
                        generateFaultDiagnoseInfo(step++, mSeries, "Loading the reconfiguration solutions from Database ...",
                                "Solution -> Deactive Mainfunction " + FaultObj.getJSONObject("execute_command").getJSONArray("mainfunction_ids")
                                        .toString());
                    } else {
                        generateFaultDiagnoseInfo(step++, mSeries,
                                "Scanning Database to check wether the fault was solved before ...",
                                "This fault was never happend before.");
                        FaultObj = handleUnknownFault(mComponetID, mSeries, fault_type, fault_desc);
                        generateFaultDiagnoseInfo(step++, mSeries, "Generating reconfiguration solutions ...",
                                "Solution -> Deactive Mainfunction " + FaultObj.getJSONObject("execute_command")
                                        .getJSONArray("mainfunction_ids").toString());
                    }
                    break;
                default:
                    break;
            }
		database.saveFaultDiagnoseProcedure(mComponetID, mSeries, fault_type, fault_desc, DiagnoseProcedureInfo.toString(), FaultObj.getJSONObject("execute_command").toString());
		return FaultObj;
	}

	private JSONObject handleKnownFault(int mComponetID, String mSeries) throws SQLException, NamingException {
		return database.getFaultInfobyComponent(mComponetID);
	}

	public JSONObject handleUnknownFault(int mComponentID, String mSeries, String fault_type, String fault_desc)
			throws SQLException, NamingException, JSONException {
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
		mMainObj.put("fault_type", fault_type);
		mMainObj.put("fault_desc", fault_desc);
		mMainObj.put("execute_command", mExecutionObj);
		mMainObj.put("insert_date", dateFormat.format(date));
		database.saveFault(mMainObj);

		generateFaultDiagnoseInfo(step++, mSeries,
				"Scanning [Deployment Matrix] to obtain [Deployment] relationed Components ...",
				"Component IDs: " + mSubsystemIDs.toString());
		generateFaultDiagnoseInfo(step++, mSeries, "Scanning [Components Model] to obtain relationed Subsystems ...",
				"Subsystem IDs: " + mSubsystemIDs.toString());
		generateFaultDiagnoseInfo(step++, mSeries, "Scanning [Requirments Model] to obtain relationed Functions ...",
				"Function IDs: " + mSubsystemIDs.toString());
		generateFaultDiagnoseInfo(step++, mSeries, "Scanning [Functions Model] to obtain relationed Mainfunctions ...",
				"Mainfunction IDs: " + mSubsystemIDs.toString());
		return mMainObj;
	}

	private JSONArray getMainfunctionIDbySubfunction(JSONArray mSubfunctionIDs) throws SQLException, NamingException {
		List<Integer> mMainfunctionID = database.getMainfunctionIDbySubfunction(mSubfunctionIDs);
		Iterator<Integer> mMainfunctionIterator = mMainfunctionID.iterator();
		JSONArray mMainfunctionArray = new JSONArray();

		while (mMainfunctionIterator.hasNext()) {
			mMainfunctionArray.put(mMainfunctionIterator.next());
		}
		return mMainfunctionArray;
	}

	private JSONArray getSubfunctionIDbyFunction(JSONArray mFunctionIDs) throws SQLException, NamingException {
		List<Integer> mSubfunctionID = database.getSubfunctionIDbyFunction(mFunctionIDs);
		Iterator<Integer> mSubfunctionIterator = mSubfunctionID.iterator();
		JSONArray mSubfunctionArray = new JSONArray();

		while (mSubfunctionIterator.hasNext()) {
			mSubfunctionArray.put(mSubfunctionIterator.next());
		}
		return mSubfunctionArray;
	}

	private JSONArray getFunctionIDbyComponent(int mComponentID) throws SQLException, NamingException {
		List<Integer> mFunctionsID = database.getFunctionIDbyComponent(mComponentID);
		Iterator<Integer> mFunctionsIterator = mFunctionsID.iterator();
		JSONArray mFunctionArray = new JSONArray();

		while (mFunctionsIterator.hasNext()) {
			mFunctionArray.put(mFunctionsIterator.next());
		}
		return mFunctionArray;
	}

	private JSONArray getSubsystemIDbyComponent(int mComponentID) throws SQLException, NamingException {
		List<Integer> mSubsystemID = database.getSubsystemIDbyComponent(mComponentID);
		Iterator<Integer> mSubsystemIterator = mSubsystemID.iterator();
		JSONArray mSubsystemArray = new JSONArray();

		while (mSubsystemIterator.hasNext()) {
			mSubsystemArray.put(mSubsystemIterator.next());
		}
		return mSubsystemArray;
	}

	public void updateStatus(JSONObject mResult) throws JSONException, NamingException, SQLException {
		database.updateComponents(mResult.getInt("component"));
		database.updateFunctions(mResult.getJSONArray("functions"));
		database.updateSubsystems(mResult.getJSONArray("subsystems"));
		database.updateSubfunctions(mResult.getJSONArray("subfunctions"));
		database.updateMainfunctions(mResult.getJSONArray("mainfunctions"));
	}

	private void generateFaultDiagnoseInfo(int step, String mPosition, String mDo, String mResult) {
		JSONObject DiagnoseStepInfo = new JSONObject();
		DiagnoseStepInfo.put("step", step);
		DiagnoseStepInfo.put("Position", mPosition);
		DiagnoseStepInfo.put("Do", mDo);
		DiagnoseStepInfo.put("Result", mResult);
		DiagnoseProcedureInfo.put(DiagnoseStepInfo);
	}
}
