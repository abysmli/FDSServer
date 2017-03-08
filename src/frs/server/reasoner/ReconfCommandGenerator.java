/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.server.reasoner;

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
        System.out.println("\nStep 1: Generating Reconfiguration Command...");
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
        System.out.println("Reconfiguration Command: ");
        System.out.println("mainfunction command: " + mMainFunctionsCommand);
        System.out.println("subfunction Command: " + mSubFunctionsCommand);
        System.out.println("basicfunction Command: " + mBasciFunctionsCommand);
        System.out.println(availableCommand.toString());
        return availableCommand;
    }

    private JSONObject RedundanzAnalyse() {
        System.out.println("\nStep 2: Redundanz Analysis");
        JSONObject obj = new JSONObject();
        obj.put("function", "F5 - F7");
        obj.put("component", "C3 - C27");
        System.out.println(obj.toString());
        return obj;
    }

    private String CheckRestartRequired(JSONObject mAvailableFunction) {
        System.out.println("\nStep 3: Check whether Restart required...");
        System.out.println("true");
        return "true";
    }

    private JSONObject ReconfFunctionGenerator(JSONObject mAvailableFunction) {
        System.out.println("\nStep 4: Generating Reconfiguration Functions...");
        System.out.println(mAvailableFunction.toString());
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
        System.out.println("\nStep 5: Generating Command for System Change...");
        System.out.println(functionFlags.toString());
        TaskAnalysis(reconfSystemChanged, mTaskList);
        return reconfSystemChanged;
    }

    private JSONObject TaskAnalysis(JSONObject reconfSystemChanged, JSONArray mTaskList) throws SQLException, NamingException {
        System.out.println("\nStep 6: Tasks Analysis...");
        databaseSystem.getTasks();
        for (int i = 0; i < mTaskList.length(); i++) {
            JSONObject taskObj = mTaskList.getJSONObject(i);
            System.out.println("\nTask Nr.: " + taskObj.getString("task_nr"));
            System.out.println("Task ID: " + taskObj.getString("task_id"));
            System.out.println("Task Name: " + taskObj.getString("task_name"));
            // todo: realize the logic
            switch (taskObj.getString("task_id")) {
                case "1":
                    System.out.println("Task '" + taskObj.getString("task_name") + "' fulfilled the resource rule: Minimum 8L Water in Tank 102");
                    System.out.println("Task '" + taskObj.getString("task_name") + "' fulfilled the function rule: MF1 = true, MF3 = true, MF4 = true");
                    mTaskList.getJSONObject(i).put("status", "normal");
                    break;
                case "2":
                    System.out.println("Task '" + taskObj.getString("task_name") + "' fulfilled the resource rule: Minimum 5L Water in Tank 102");
                    System.out.println("Task '" + taskObj.getString("task_name") + "' fulfilled the function rule: MF3 = true");
                    mTaskList.getJSONObject(i).put("status", "normal");
                    break;
                case "3":
                    System.out.println("Task '" + taskObj.getString("task_name") + "' fulfilled the resource rule: Minimum 8L Water in Tank 102");
                    System.out.println("Task '" + taskObj.getString("task_name") + "' fulfilled the function rule: MF2 = true");
                    mTaskList.getJSONObject(i).put("status", "normal");
                    break;
                case "4":
                    System.out.println("Task '" + taskObj.getString("task_name") + "' fulfilled the resource rule: Minimum 3L Water in Tank 102");
                    System.out.println("Task '" + taskObj.getString("task_name") + "' fulfilled the function rule: MF1 = true, MF3 = true");
                    mTaskList.getJSONObject(i).put("status", "normal");
                    break;
                default:
                    break;
            }
        }
        System.out.println("\nTask List after analysis: ");
        System.out.println(mTaskList.toString());
        reconfSystemChanged.put("task_list", mTaskList);
        return reconfSystemChanged;
    }

    private String SpecialCodeGenerator(JSONObject mAvailableFunction) {
        System.out.println("\nStep 7: Generating Special Code...");
        System.out.println("Special Code: 'temp = temperaturDisplay2.getTemperatur()'");
        return "temp = temperaturDisplay2.getTemperatur()";
    }

    private JSONObject PersonalData() {
        System.out.println("\nStep 8: Get Personal Data");
        JSONObject obj = new JSONObject();
        obj.put("General Techniker", "Wang, Huiqiang +49 123 4567 899");
        obj.put("Wartungsdienst", "Hui, Wangqiang +49 321 2233 899");
        obj.put("Expert", "Qiang, Wangqiang +49 333 4567 888");
        System.out.println(obj.toString());
        return obj;
    }
}
