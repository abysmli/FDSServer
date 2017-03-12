/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.server.reasoner;

import frs.server.controller.AnalysisProcedureGenerator;
import frs.server.model.SystemDatabaseHandler;
import java.sql.SQLException;
import javax.naming.NamingException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author abysmli
 */
public class ReconfCommandGenerator {

    private final SystemDatabaseHandler databaseSystem = new SystemDatabaseHandler();
    private final AnalysisProcedureGenerator analysisProcedure;

    public ReconfCommandGenerator(AnalysisProcedureGenerator analysisProcedure) {
        this.analysisProcedure = analysisProcedure;
    }
    

    public JSONObject generate(JSONObject mAvailableFunction, JSONArray mTaskList) throws SQLException, NamingException {
        JSONObject resultObj = new JSONObject();
        resultObj.put("command", this.ReconfigurationCommandGenerator(mAvailableFunction));
        resultObj.put("redundanz", this.RedundanzAnalyse());
        resultObj.put("restart", this.CheckRestartRequired(mAvailableFunction));
        resultObj.put("reconf_function", this.ReconfFunctionGenerator(mAvailableFunction));
        resultObj.put("reconf_systemchange", this.ReconfSystemChangeGenerator(mAvailableFunction, mTaskList));
        resultObj.put("special_code", this.SpecialCodeGenerator(mAvailableFunction));
        resultObj.put("personal_data", this.PersonalData());
        return resultObj;
    }

    private JSONArray ReconfigurationCommandGenerator(JSONObject mAvailableFunction) {
        analysisProcedure.write("\nStep 1: Generating Reconfiguration Command...");
        JSONArray mMainFunctions = mAvailableFunction.getJSONArray("main_functions");
        JSONArray mSubFunctions = mAvailableFunction.getJSONArray("sub_functions");
        JSONArray mBasicFunctions = mAvailableFunction.getJSONArray("basic_functions");
        String mMainFunctionsCommand = new String("0x");
        for (int i = 0; i < mMainFunctions.length(); i++) {
            JSONObject obj = mMainFunctions.getJSONObject(i);
            if (obj.getString("availability").equals("true")) {
                mMainFunctionsCommand += "1";
            } else {
                mMainFunctionsCommand += "0";
            }
        }
        String mSubFunctionsCommand = new String("0x");
        for (int i = 0; i < mSubFunctions.length(); i++) {
            JSONObject obj = mSubFunctions.getJSONObject(i);
            if (obj.getString("availability").equals("true")) {
                mSubFunctionsCommand += "1";
            } else {
                mSubFunctionsCommand += "0";
            }
        }
        String mBasciFunctionsCommand = new String("0x");
        for (int i = 0; i < mBasicFunctions.length(); i++) {
            JSONObject obj = mBasicFunctions.getJSONObject(i);
            if (obj.getString("availability").equals("true")) {
                mBasciFunctionsCommand += "1";
            } else {
                mBasciFunctionsCommand += "0";
            }
        }
        JSONObject mainFunctionCommand = new JSONObject();
        JSONObject subFunctionCommand = new JSONObject();
        JSONObject basicFunctionCommand = new JSONObject();
        mainFunctionCommand.put("main_function_command", mMainFunctionsCommand);
        subFunctionCommand.put("sub_function_command", mSubFunctionsCommand);
        basicFunctionCommand.put("basic_function_command", mBasciFunctionsCommand);
        JSONArray availableCommand = new JSONArray();
        availableCommand.put(mainFunctionCommand);
        availableCommand.put(subFunctionCommand);
        availableCommand.put(basicFunctionCommand);
        analysisProcedure.write("Reconfiguration Command: ");
        analysisProcedure.write("mainfunction command: " + mMainFunctionsCommand);
        analysisProcedure.write("subfunction Command: " + mSubFunctionsCommand);
        analysisProcedure.write("basicfunction Command: " + mBasciFunctionsCommand);
        analysisProcedure.write(availableCommand.toString());
        analysisProcedure.reconfigurationInfo.setReconfigurationsCommand(availableCommand);
        return availableCommand;
    }

    private JSONObject RedundanzAnalyse() {
        analysisProcedure.write("\nStep 2: Redundanz Analysis");
        JSONObject obj = new JSONObject();
        obj.put("function", "F5 - F7");
        obj.put("component", "C3 - C27");
        analysisProcedure.write(obj.toString());
        analysisProcedure.reconfigurationInfo.setRedundanzAnalysis(obj);
        return obj;
    }

    private String CheckRestartRequired(JSONObject mAvailableFunction) {
        analysisProcedure.write("\nStep 3: Check whether Restart required...");
        analysisProcedure.write("true");
        analysisProcedure.reconfigurationInfo.setRestart("true");
        return "true";
    }

    private JSONObject ReconfFunctionGenerator(JSONObject mAvailableFunction) {
        analysisProcedure.write("\nStep 4: Generating Reconfiguration Functions...");
        analysisProcedure.write(mAvailableFunction.toString());
        analysisProcedure.reconfigurationInfo.setReconfigurationFunctions(mAvailableFunction);
        return mAvailableFunction;
    }

    private JSONObject ReconfSystemChangeGenerator(JSONObject mAvailableFunction, JSONArray mTaskList) throws SQLException, NamingException {
        JSONArray BasicFunctions = mAvailableFunction.getJSONArray("basic_functions");
        JSONArray SubFunctions = mAvailableFunction.getJSONArray("sub_functions");
        JSONArray MainFunctions = mAvailableFunction.getJSONArray("main_functions");

        JSONObject reconfSystemChanged = new JSONObject();
        JSONArray functionFlags = new JSONArray();
        for (int i = 0; i < BasicFunctions.length(); i++) {
            JSONObject basciFunction = BasicFunctions.getJSONObject(i);
            JSONObject obj = new JSONObject();
            if (basciFunction.getString("availability").equals("true")) {
                obj.put("Function_Flag_" + String.valueOf(basciFunction.getInt("function_id")), "true");
            } else {
                obj.put("Function_Flag_" + String.valueOf(basciFunction.getInt("function_id")), "false");
            }
            functionFlags.put(obj);
        }
        reconfSystemChanged.put("functions_flag", functionFlags);
        analysisProcedure.write("\nStep 5: Generating Command for System Change...");
        analysisProcedure.write(functionFlags.toString());
        analysisProcedure.reconfigurationInfo.setReconfigurationSystemchange(functionFlags);
        TaskAnalysis(reconfSystemChanged, mTaskList);
        return reconfSystemChanged;
    }

    private JSONObject TaskAnalysis(JSONObject reconfSystemChanged, JSONArray mTaskList) throws SQLException, NamingException {
        analysisProcedure.write("\nStep 6: Tasks Analysis...");
        databaseSystem.getTasks();
        JSONArray mTaskAnalysis = new JSONArray();
        for (int i = 0; i < mTaskList.length(); i++) {
            JSONObject taskObj = mTaskList.getJSONObject(i);
            analysisProcedure.write("\nTask Nr.: " + taskObj.getString("task_nr"));
            analysisProcedure.write("Task ID: " + taskObj.getString("task_id"));
            analysisProcedure.write("Task Name: " + taskObj.getString("task_name"));
            // todo: realize the logic
            
            JSONObject mTaskAnalysisObj = new JSONObject();
            mTaskAnalysisObj.put("task_nr", String.valueOf(i));
            mTaskAnalysisObj.put("task_id", taskObj.getString("task_id"));
            mTaskAnalysisObj.put("task_name", taskObj.getString("task_name"));
            switch (taskObj.getString("task_id")) {
                case "1":
                    mTaskAnalysisObj.put("resource_rule", "Minimum 8L Water in Tank 102");
                    mTaskAnalysisObj.put("resource_rule_result", "fulfilled");
                    mTaskAnalysisObj.put("function_rule", "MF1 = true, MF3 = true, MF4 = true");
                    mTaskAnalysisObj.put("function_rule_result", "fulfilled");
                    analysisProcedure.write("Task " + taskObj.getString("task_name") + " fulfilled the resource rule: Minimum 8L Water in Tank 102");
                    analysisProcedure.write("Task " + taskObj.getString("task_name") + " fulfilled the function rule: MF1 = true, MF3 = true, MF4 = true");
                    mTaskList.getJSONObject(i).put("status", "normal");
                    break;
                case "2":
                    mTaskAnalysisObj.put("resource_rule", "Minimum 5L Water in Tank 102");
                    mTaskAnalysisObj.put("resource_rule_result", "fulfilled");
                    mTaskAnalysisObj.put("function_rule", "MF3 = true");
                    mTaskAnalysisObj.put("function_rule_result", "fulfilled");
                    analysisProcedure.write("Task " + taskObj.getString("task_name") + " fulfilled the resource rule: Minimum 5L Water in Tank 102");
                    analysisProcedure.write("Task " + taskObj.getString("task_name") + " fulfilled the function rule: MF3 = true");
                    mTaskList.getJSONObject(i).put("status", "normal");
                    break;
                case "3":
                    mTaskAnalysisObj.put("resource_rule", "Minimum 8L Water in Tank 102");
                    mTaskAnalysisObj.put("resource_rule_result", "fulfilled");
                    mTaskAnalysisObj.put("function_rule", "MF2 = true");
                    mTaskAnalysisObj.put("function_rule_result", "fulfilled");
                    analysisProcedure.write("Task " + taskObj.getString("task_name") + " fulfilled the resource rule: Minimum 8L Water in Tank 102");
                    analysisProcedure.write("Task " + taskObj.getString("task_name") + " fulfilled the function rule: MF2 = true");
                    mTaskList.getJSONObject(i).put("status", "normal");
                    break;
                case "4":
                    mTaskAnalysisObj.put("resource_rule", "Minimum 3L Water in Tank 102");
                    mTaskAnalysisObj.put("resource_rule_result", "fulfilled");
                    mTaskAnalysisObj.put("function_rule", "MF1 = true, MF3 = true");
                    mTaskAnalysisObj.put("function_rule_result", "fulfilled");
                    analysisProcedure.write("Task " + taskObj.getString("task_name") + " fulfilled the resource rule: Minimum 3L Water in Tank 102");
                    analysisProcedure.write("Task " + taskObj.getString("task_name") + " fulfilled the function rule: MF1 = true, MF3 = true");
                    mTaskList.getJSONObject(i).put("status", "normal");
                    break;
                default:
                    break;
            }
            mTaskAnalysis.put(mTaskAnalysisObj);
        }
        analysisProcedure.write("\nTask List after analysis: ");
        analysisProcedure.write(mTaskList.toString());
        analysisProcedure.reconfigurationInfo.setTaskAnalysis(mTaskAnalysis);
        analysisProcedure.reconfigurationInfo.setTaskList(mTaskList);
        reconfSystemChanged.put("task_list", mTaskList);
        return reconfSystemChanged;
    }

    private String SpecialCodeGenerator(JSONObject mAvailableFunction) {
        analysisProcedure.write("\nStep 7: Generating Special Code...");
        analysisProcedure.write("Special Code: temp = temperaturDisplay2.getTemperatur()");
        analysisProcedure.reconfigurationInfo.setSpecialCode("temp = temperaturDisplay2.getTemperatur()");
        return "temp = temperaturDisplay2.getTemperatur()";
    }

    private JSONObject PersonalData() {
        analysisProcedure.write("\nStep 8: Get Personal Data");
        JSONObject obj = new JSONObject();
        obj.put("General Techniker", "Wang, Huiqiang +49 123 4567 899");
        obj.put("Wartungsdienst", "Hui, Wangqiang +49 321 2233 899");
        obj.put("Expert", "Qiang, Wangqiang +49 333 4567 888");
        analysisProcedure.write(obj.toString());
        analysisProcedure.reconfigurationInfo.setPersonalData(obj);
        return obj;
    }
}
