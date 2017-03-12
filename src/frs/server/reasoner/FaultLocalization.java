/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.server.reasoner;

import frs.server.controller.AnalysisProcedureGenerator;
import frs.server.model.FaultDatabaseHandler;
import frs.server.model.SymptomDatabaseHandler;
import frs.server.model.SystemDatabaseHandler;
import java.sql.SQLException;
import javax.naming.NamingException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author abysmli
 */
public class FaultLocalization {

    private final SymptomDatabaseHandler databaseSymptom = new SymptomDatabaseHandler();
    private final FaultDatabaseHandler databaseFault = new FaultDatabaseHandler();
    private final SystemDatabaseHandler databaseSystem = new SystemDatabaseHandler();
    private final AnalysisProcedureGenerator analysisProcedure;

    public FaultLocalization(AnalysisProcedureGenerator analysisProcedure) {
        this.analysisProcedure = analysisProcedure;
    }
    
    public JSONObject getFaultLocation(String faultLocation, String faultType, String faultParam, String faultValue, String equipmentID) throws SQLException, NamingException {
        JSONObject resultObj = new JSONObject();
        int mFaultSubsystem = 0;
        JSONArray symptomSubsystem = databaseSymptom.getSymptomSubsystem();
        analysisProcedure.write("\nSymtom Analysis Step1: Compare Symptom Subsystem");
        for (int i=0; i<symptomSubsystem.length(); i++) {
            if (symptomSubsystem.getJSONObject(i).getString("parameter").equals(faultParam)) {
                mFaultSubsystem = symptomSubsystem.getJSONObject(i).getInt("subsystem_id");
            }
        }
        analysisProcedure.faultLocalizationInfo.setFaultParameter(faultParam);
        analysisProcedure.write("Found Fault Parameter: " + faultParam);
        switch (mFaultSubsystem) {
            case 1:
                analysisProcedure.write("Found: Fault in Subsystem Heating...");
                analysisProcedure.write("\nSymtom Analysis Step2: Compare Symptom Subsystem Heating");
                resultObj = symptomHeatingAnalysis(faultLocation, faultType, faultValue, equipmentID);
                analysisProcedure.write(resultObj.toString());
                break;
            case 2:
                analysisProcedure.write("Found: Fault in Subsystem Inflow...");
                analysisProcedure.write("\nSymtom Analysis Step2: Compare Symptom Subsystem Inflow");
                resultObj = symptomInflowAnalysis(faultLocation, faultType, faultValue, equipmentID);
                analysisProcedure.write(resultObj.toString());
                break;
            case 3:
                analysisProcedure.write("Found: Fault in Subsystem Pumping...");
                analysisProcedure.write("\nSymtom Analysis Step2: Compare Symptom Subsystem Pumping");
                resultObj = symptomPumpingAnalysis(faultLocation, faultType, faultValue, equipmentID);
                analysisProcedure.write(resultObj.toString());
                break;
            case 4:
                analysisProcedure.write("Found: Fault in Subsystem Airpressure...");
                analysisProcedure.write("\nSymtom Analysis Step2: Compare Symptom Subsystem Airpressure");
                resultObj = symptomAirpressureAnalysis(faultLocation, faultType, faultValue, equipmentID);
                analysisProcedure.write(resultObj.toString());
                break;
            default:
                break;
        }
        analysisProcedure.faultLocalizationInfo.setSymptom(resultObj);
        return resultObj;
    }

    
    private JSONObject symptomHeatingAnalysis(String faultLocation, String faultType, String faultValue, String equipmentID) {
        JSONObject resultObj = new JSONObject();
        if (faultType.equals("value")) {
            resultObj.put("symptom_id", 2);
            resultObj.put("symptom_desc", "Temperatur bigger than 100 Degress");
            resultObj.put("fault_location", "3");
        } else if (faultType.equals("changerate")) {
            resultObj.put("symptom_id", 4);
            resultObj.put("symptom_desc", "Temperatur changerate less than 0.5 Degress/s");
            resultObj.put("fault_location", "3");
        }
        analysisProcedure.write("Compare Symptom Subsystem Heating Result dump: ");
        return resultObj;
    }

    private JSONObject symptomAirpressureAnalysis(String faultLocation, String faultType, String faultValue, String equipmentID) {
        JSONObject resultObj = new JSONObject();
        if (faultType == "value") {
            resultObj.put("symptom_id", 22);
            resultObj.put("symptom_desc", "Airpressure bigger than 6 Pa");
            resultObj.put("fault_location", "3");
        } else if (faultType == "changerate") {
            resultObj.put("symptom_id", 22);
            resultObj.put("symptom_desc", "Airpressure changerate less than 0.5 Pa/s");
            resultObj.put("fault_location", "3");
        }
        return resultObj;
    }

    private JSONObject symptomPumpingAnalysis(String faultLocation, String faultType, String faultValue, String equipmentID) {
        JSONObject resultObj = new JSONObject();
        if (faultType == "value") {
            resultObj.put("symptom_id", 15);
            resultObj.put("symptom_desc", "Water flowrate bigger than 1 L/s");
            resultObj.put("fault_location", "3");
        } else if (faultType == "changerate") {
            resultObj.put("symptom_id", 14);
            resultObj.put("symptom_desc", "Water pressure changerate big than 10 Pa/s");
            resultObj.put("fault_location", "3");
        }
        return resultObj;
    }

    private JSONObject symptomInflowAnalysis(String faultLocation, String faultType, String faultValue, String equipmentID) {
        JSONObject resultObj = new JSONObject();
        if (faultType == "value") {
            resultObj.put("symptom_id", 8);
            resultObj.put("symptom_desc", "Water level bigger than 9 L");
            resultObj.put("fault_location", "3");
        } else if (faultType == "changerate") {
            resultObj.put("symptom_id", 8);
            resultObj.put("symptom_desc", "Water level changerate bigger than 2 L/s");
            resultObj.put("fault_location", "3");
        }
        return resultObj;
    }
    
}
