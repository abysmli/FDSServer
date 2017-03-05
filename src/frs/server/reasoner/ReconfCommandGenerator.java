/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.server.reasoner;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author abysmli
 */
public class ReconfCommandGenerator {

    public JSONObject generate(JSONObject mAvailableFunction) {
        JSONObject resultObj = new JSONObject();
        resultObj.put("command", this.ReconfigurationCommandGenerator(mAvailableFunction));
        resultObj.put("restart", this.CheckRestartRequired(mAvailableFunction));
        resultObj.put("reconf_function", this.ReconfFunctionGenerator(mAvailableFunction));
        resultObj.put("reconf_systemchange", this.ReconfSystemChangeGenerator(mAvailableFunction));
        resultObj.put("special_code", this.SpecialCodeGenerator(mAvailableFunction));
        return resultObj;
    }
    
    private JSONArray ReconfigurationCommandGenerator(JSONObject mAvailableFunction) {
        System.out.println("\nStep 1: Generating Reconfiguration Command...");
        JSONArray mMainFunctions = mAvailableFunction.getJSONArray("main_functions");
        JSONArray mSubFunctions = mAvailableFunction.getJSONArray("sub_functions");
        JSONArray mBasicFunctions = mAvailableFunction.getJSONArray("basic_functions");
        String mMainFunctionsCommand = new String("0x");
        for (int i=0; i<mMainFunctions.length();i++) {
            JSONObject obj = mMainFunctions.getJSONObject(i);
            if (obj.getString("availability").equals("true")) {
                mMainFunctionsCommand += "1";
            } else {
                mMainFunctionsCommand += "0";
            }
        }
        String mSubFunctionsCommand = new String("0x");
        for (int i=0; i<mSubFunctions.length();i++) {
            JSONObject obj = mSubFunctions.getJSONObject(i);
            if (obj.getString("availability").equals("true")) {
                mSubFunctionsCommand += "1";
            } else {
                mSubFunctionsCommand += "0";
            }
        }
        String mBasciFunctionsCommand = new String("0x");
        for (int i=0; i<mBasicFunctions.length();i++) {
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
    
    private String CheckRestartRequired(JSONObject mAvailableFunction) {
        System.out.println("\nStep 2: Check whether Restart required...");
        System.out.println("true");
        return "true";
    }
    
    private JSONObject ReconfFunctionGenerator(JSONObject mAvailableFunction) {
        System.out.println("\nStep 3: Generating Reconfiguration Functions...");
        System.out.println(mAvailableFunction.toString());
        return mAvailableFunction;
    }
    
    private JSONArray ReconfSystemChangeGenerator(JSONObject mAvailableFunction) {
        JSONArray BasicFunctions = mAvailableFunction.getJSONArray("basic_functions");
        JSONArray SubFunctions = mAvailableFunction.getJSONArray("sub_functions");
        JSONArray MainFunctions = mAvailableFunction.getJSONArray("main_functions");
        
        JSONArray reconfSystemChanged = new JSONArray();
        for (int i=0; i< BasicFunctions.length();i++) {
            JSONObject basciFunction = BasicFunctions.getJSONObject(i);
            JSONObject obj = new JSONObject();
            if (basciFunction.getString("availability").equals("true")) {
                obj.put("Function_Flag_" + String.valueOf(basciFunction.getInt("function_id")), "true");
            } else {
                obj.put("Function_Flag_" + String.valueOf(basciFunction.getInt("function_id")), "false");
            }
            reconfSystemChanged.put(obj);
        }
        System.out.println("\nStep 4: Generating Command for System Change...");
        System.out.println(reconfSystemChanged.toString());
        return reconfSystemChanged;
    }
    
    private String SpecialCodeGenerator(JSONObject mAvailableFunction) {
        System.out.println("\nStep 5: Generating Special Code...");
        System.out.println("Special Code: 'temp = temperaturDisplay2.getTemperatur()'");
        return "temp = temperaturDisplay2.getTemperatur()";
    }
    
}
