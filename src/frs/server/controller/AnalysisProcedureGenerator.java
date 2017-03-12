/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.server.controller;

import frs.server.model.SystemDatabaseHandler;
import java.sql.SQLException;
import javax.naming.NamingException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author abysmli
 */
public class AnalysisProcedureGenerator {

    private final JSONObject mTaskAnalysis = new JSONObject();
    private final SystemDatabaseHandler systemDatabase = new SystemDatabaseHandler();

    private final JSONArray mDump = new JSONArray();
    private int mDumpLineNr = 0;

    public FaultInfo faultInfo = new FaultInfo();
    public FaultLocalizationInfo faultLocalizationInfo = new FaultLocalizationInfo();
    public FunctionAnalysisInfo functionAnalysisInfo = new FunctionAnalysisInfo();
    public ReconfigurationInfo reconfigurationInfo = new ReconfigurationInfo();

    public void write(String mInfo) {
        mDumpLineNr++;
        mDump.put(new JSONObject().put(String.valueOf(mDumpLineNr), mInfo));
        System.out.println(mInfo);
    }
    
    public void setFaultType(String mInfo) {
        mTaskAnalysis.put("fault_type", mInfo);
    }

    public void setResult(JSONObject mObj) {
        mTaskAnalysis.put("result", mObj);
    }

    public void save() throws NamingException, SQLException {
        mTaskAnalysis.put("fault_info", faultInfo.getFaultInfo());
        mTaskAnalysis.put("fault_localization_info", faultLocalizationInfo.getFaultLocalizationInfo());
        mTaskAnalysis.put("function_analysis_info", functionAnalysisInfo.getFunctionAnalysisInfo());
        mTaskAnalysis.put("reconfiguration_info", reconfigurationInfo.getReconfigurationInfo());
        mTaskAnalysis.put("dump_info", mDump);
        systemDatabase.saveAnalysisProcedure(mTaskAnalysis);
    }

    public class FaultInfo {

        private final JSONObject mFaultInfo = new JSONObject();

        public void setFaultInfo(String mInfo) {
            mFaultInfo.put("fault_info", mInfo);
        }

        public void setFaultObject(JSONObject mObj) {
            mFaultInfo.put("fault_object", mObj);
        }

        public JSONObject getFaultInfo() {
            return this.mFaultInfo;
        }
    }

    public class FaultLocalizationInfo {

        private final JSONObject mFaultLocalizationInfo = new JSONObject();

        public void setFaultParameter(String mInfo) {
            mFaultLocalizationInfo.put("fault_parameter", mInfo);
        }

        public void setSymptom(JSONObject mObj) {
            mFaultLocalizationInfo.put("symptom", mObj);
        }
        
        public void setFaultLocation(String mInfo) {
            mFaultLocalizationInfo.put("fault_location", mInfo);
        }

        public JSONObject getFaultLocalizationInfo() {
            return this.mFaultLocalizationInfo;
        }
    }

    public class FunctionAnalysisInfo {

        private final JSONObject mFunctionAnalysisInfo = new JSONObject();

        public void setFunctionAnalysis(JSONArray mObj) {
            mFunctionAnalysisInfo.put("function_analysis", mObj);
        }

        public void setRequirementAnalysis(JSONArray mObj) {
            mFunctionAnalysisInfo.put("requirement_analysis", mObj);
        }
        
        public void setBasicFunctionAvailability(JSONArray mObj) {
            mFunctionAnalysisInfo.put("basic_functions", mObj);
        }
        
        public void setSubFunctionAvailability(JSONArray mObj) {
            mFunctionAnalysisInfo.put("sub_functions", mObj);
        }
        
        public void setMainFunctionAvailability(JSONArray mObj) {
            mFunctionAnalysisInfo.put("main_functions", mObj);
        }
        
        public JSONObject getFunctionAnalysisInfo() {
            return this.mFunctionAnalysisInfo;
        }
    }

    public class ReconfigurationInfo {

        private final JSONObject mReconfigurationInfo = new JSONObject();

        public void setReconfigurationsCommand(JSONArray mObj) {
            mReconfigurationInfo.put("command", mObj);
        }

        public void setRedundanzAnalysis(JSONObject mObj) {
            mReconfigurationInfo.put("redundanz", mObj);
        }
        
        public void setRestart(String mInfo) {
            mReconfigurationInfo.put("restart", mInfo);
        }
        
        public void setReconfigurationFunctions(JSONObject mObj) {
            mReconfigurationInfo.put("reconf_functions", mObj);
        }
        
        public void setReconfigurationSystemchange(JSONArray mObj) {
            mReconfigurationInfo.put("reconf_systemchange", mObj);
        }
        
        public void setTaskAnalysis(JSONArray mObj) {
            mReconfigurationInfo.put("task_analysis", mObj);
        }

        public void setTaskList(JSONArray mObj) {
            mReconfigurationInfo.put("task_list", mObj);
        }
        
        public void setSpecialCode(String mInfo) {
            mReconfigurationInfo.put("special_code", mInfo);
        }
        
        public void setPersonalData(JSONObject mObj) {
            mReconfigurationInfo.put("personal_data", mObj);
        }
        
        public JSONObject getReconfigurationInfo() {
            return this.mReconfigurationInfo;
        }
    }

}
