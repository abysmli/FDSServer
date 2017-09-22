package frs.server.controller;

import frs.server.model.FaultDatabaseHandler;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import frs.server.model.SymptomDatabaseHandler;
import frs.server.model.SystemDatabaseHandler;
import frs.server.reasoner.FaultLocalization;
import frs.server.reasoner.FunctionAnalysis;
import frs.server.reasoner.ReconfCommandGenerator;

/**
 * @author Li, Yuan Project: FDSServer
 */
public class FaultController {

    private final SymptomDatabaseHandler databaseSymptom = new SymptomDatabaseHandler();
    private final FaultDatabaseHandler databaseFault = new FaultDatabaseHandler();
    private final SystemDatabaseHandler databaseSystem = new SystemDatabaseHandler();
    
    public final AnalysisProcedureGenerator analysisProcedure = new AnalysisProcedureGenerator();
    private final FaultLocalization faultLocalization = new FaultLocalization(analysisProcedure);
    private final FunctionAnalysis functionAnalysis = new FunctionAnalysis(analysisProcedure);
    private final ReconfCommandGenerator reconfCommandGenerator = new ReconfCommandGenerator(analysisProcedure);
    
    JSONArray DiagnoseProcedureInfo = new JSONArray();
    
    public JSONObject handleFault(JSONObject mFaultObj)
            throws SQLException, NamingException {
        
        analysisProcedure.faultInfo.setFaultObject(mFaultObj);
        String faultEffect = mFaultObj.getString("fault_effect");
        String faultName = mFaultObj.getString("fault_name");
        String faultMessage = mFaultObj.getString("fault_message");
        String faultParam = mFaultObj.getString("fault_parameter");
        String faultValue = mFaultObj.getString("fault_value");
        String faultType = mFaultObj.getString("fault_type");
        analysisProcedure.setFaultType(faultType);
        String faultLocation = mFaultObj.getString("fault_location");
        String equipmentID = mFaultObj.getString("equipment_id");
        JSONArray mTaskList = mFaultObj.getJSONArray("task_list");
        boolean knownFaultFlag = false;
        JSONArray faultObjs = databaseFault.getFaultKnowledge();
        JSONObject resultObj = new JSONObject();
//        for (int i = 0; i < faultObjs.length(); i++) {
//            if (faultObjs.getJSONObject(i).getString("fault_location").equals(faultLocation)) {
//                knownFaultFlag = true;
//                analysisProcedure.faultInfo.setFaultInfo("Known Fault detected!");
//                analysisProcedure.write("Known Fault detected!");
//                System.out.println();
//                analysisProcedure.write("Following Data will generated from Database: ");
//                resultObj = faultObjs.getJSONObject(i);
//                JSONObject reconfCommand = new JSONObject(resultObj.getString("reconf_command"));
//                JSONObject availableFunctions = new JSONObject(resultObj.getString("available_functions"));
//                resultObj.put("reconf_command", reconfCommand);
//                resultObj.put("available_functions", availableFunctions);
//                System.out.println(resultObj.toString());
//            }
//        }
        if (knownFaultFlag) {
            return resultObj;
        } else {
            return this.handleUnknownFault(faultLocation, faultType, faultParam, faultValue, equipmentID, faultEffect, faultName, faultMessage, mTaskList);
        }
    }

    private JSONObject handleUnknownFault(String faultLocation, String faultType, String faultParam, String faultValue, String equipmentID, String faultEffect, String faultName, String faultMessage, JSONArray mTaskList) throws SQLException, NamingException {
        JSONObject resultObj = new JSONObject();
        analysisProcedure.faultInfo.setFaultInfo("Unknown Fault detected!");
        analysisProcedure.write("Unknown Fault detected!");
        System.out.println("\n\n");
        analysisProcedure.write("Now goto Fault Localization process...");
        JSONObject mFaultLocation = faultLocalization.getFaultLocation(faultLocation, faultType, faultParam, faultValue, equipmentID);
        
        System.out.println("\n\n");
        analysisProcedure.write("Now goto Function Analysis process...");
        JSONObject mAvailableFunction = functionAnalysis.analysis(mFaultLocation.getString("fault_location"));
        
        System.out.println("\n\n");
        analysisProcedure.write("Now goto Reconfiguration Commands Generation process...");
        JSONObject mReconfiguration = reconfCommandGenerator.generate(mAvailableFunction, mTaskList);
        System.out.println();
        analysisProcedure.write("Save new generated Reconfiguration Commands into Database...");
        databaseSystem.saveReconfigurations(mReconfiguration);
        
        System.out.println("\n\n");
        analysisProcedure.write("Now generate Result and save the result to Database...");
        resultObj.put("fault_no", 0);
        resultObj.put("fault_name", faultName);
        resultObj.put("symptom_id", mFaultLocation.getInt("symptom_id"));
        resultObj.put("symptom_desc", mFaultLocation.getString("symptom_desc"));
        resultObj.put("available_functions", mAvailableFunction);
        resultObj.put("reconf_command", mReconfiguration);
        resultObj.put("fault_effect", faultEffect);
        resultObj.put("fault_parameter", faultParam);
        resultObj.put("fault_value", faultValue);
        resultObj.put("fault_location", faultLocation);
        resultObj.put("fault_message", "-");
        resultObj.put("check_status", "-");
        resultObj.put("equipment_id", "-");
        resultObj.put("occured_at", (new java.util.Date()).toString());
        System.out.println();
        analysisProcedure.write("Generated Fault Knowledge: ");
        System.out.println(resultObj.toString());
        analysisProcedure.setResult(resultObj);
        databaseFault.saveFaultKnowledge(resultObj);
        
        System.out.println("\n\n");
        analysisProcedure.write("Now send the Result back to Simulator...");
        analysisProcedure.save();
        return resultObj;
    }

    private JSONArray getMainfunctionIDbySubfunction(JSONArray mSubfunctionIDs) throws SQLException, NamingException {
        List<Integer> mMainfunctionID = databaseSystem.getMainfunctionIDbySubfunction(mSubfunctionIDs);
        Iterator<Integer> mMainfunctionIterator = mMainfunctionID.iterator();
        JSONArray mMainfunctionArray = new JSONArray();

        while (mMainfunctionIterator.hasNext()) {
            mMainfunctionArray.put(mMainfunctionIterator.next());
        }
        return mMainfunctionArray;
    }

    private JSONArray getSubfunctionIDbyFunction(JSONArray mFunctionIDs) throws SQLException, NamingException {
        List<Integer> mSubfunctionID = databaseSystem.getSubfunctionIDbyFunction(mFunctionIDs);
        Iterator<Integer> mSubfunctionIterator = mSubfunctionID.iterator();
        JSONArray mSubfunctionArray = new JSONArray();

        while (mSubfunctionIterator.hasNext()) {
            mSubfunctionArray.put(mSubfunctionIterator.next());
        }
        return mSubfunctionArray;
    }

    private JSONArray getFunctionIDbyComponent(int mComponentID) throws SQLException, NamingException {
        List<Integer> mFunctionsID = databaseSystem.getFunctionIDbyComponent(mComponentID);
        Iterator<Integer> mFunctionsIterator = mFunctionsID.iterator();
        JSONArray mFunctionArray = new JSONArray();

        while (mFunctionsIterator.hasNext()) {
            mFunctionArray.put(mFunctionsIterator.next());
        }
        return mFunctionArray;
    }

    private JSONArray getSubsystemIDbyComponent(int mComponentID) throws SQLException, NamingException {
        List<Integer> mSubsystemID = databaseSystem.getSubsystemIDbyComponent(mComponentID);
        Iterator<Integer> mSubsystemIterator = mSubsystemID.iterator();
        JSONArray mSubsystemArray = new JSONArray();

        while (mSubsystemIterator.hasNext()) {
            mSubsystemArray.put(mSubsystemIterator.next());
        }
        return mSubsystemArray;
    }

    public void updateStatus(JSONObject mResult) throws JSONException, NamingException, SQLException {
        databaseSystem.updateComponents(mResult.getInt("component"));
        databaseSystem.updateFunctions(mResult.getJSONArray("functions"));
        databaseSystem.updateSubsystems(mResult.getJSONArray("subsystems"));
        databaseSystem.updateSubfunctions(mResult.getJSONArray("subfunctions"));
        databaseSystem.updateMainfunctions(mResult.getJSONArray("mainfunctions"));
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
