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
        System.out.println();
        analysisProcedure.write("Symtom Analysis Step1: Compare Symptom Subsystem");
        for (int i = 0; i < symptomSubsystem.length(); i++) {
            if (symptomSubsystem.getJSONObject(i).getString("parameter").equals(faultParam)) {
                mFaultSubsystem = symptomSubsystem.getJSONObject(i).getInt("subsystem_id");
            }
        }
        analysisProcedure.faultLocalizationInfo.setFaultParameter(faultParam);
        analysisProcedure.write("Found Fault Parameter: " + faultParam);
        switch (mFaultSubsystem) {
            case 1:
                analysisProcedure.write("Found: Fault in Subsystem Heating...");
                System.out.println();
                analysisProcedure.write("Symtom Analysis Step2: Compare Symptom Subsystem Heating");
                resultObj = symptomHeatingAnalysis(faultLocation, faultType, faultParam, faultValue, equipmentID);
                break;
            case 2:
                analysisProcedure.write("Found: Fault in Subsystem Inflow...");
                System.out.println();
                analysisProcedure.write("Symtom Analysis Step2: Compare Symptom Subsystem Inflow");
                resultObj = symptomInflowAnalysis(faultLocation, faultType, faultParam, faultValue, equipmentID);
                break;
            case 3:
                analysisProcedure.write("Found: Fault in Subsystem Pumping...");
                System.out.println();
                analysisProcedure.write("Symtom Analysis Step2: Compare Symptom Subsystem Pumping");
                resultObj = symptomPumpingAnalysis(faultLocation, faultType, faultParam, faultValue, equipmentID);
                break;
            case 4:
                analysisProcedure.write("Found: Fault in Subsystem Airpressure...");
                System.out.println();
                analysisProcedure.write("Symtom Analysis Step2: Compare Symptom Subsystem Airpressure");
                resultObj = symptomAirpressureAnalysis(faultLocation, faultType, faultParam, faultValue, equipmentID);
                break;
            default:
                break;
        }
        analysisProcedure.faultLocalizationInfo.setSymptom(resultObj);
        return resultObj;
    }

    private JSONObject symptomHeatingAnalysis(String faultLocation, String faultType, String faultParam, String faultValue, String equipmentID) throws SQLException, NamingException {
        JSONObject resultObj = new JSONObject();
        JSONArray mHeatingSymtpom = databaseSymptom.getSymptomHeating();
        for (int i = 0; i < mHeatingSymtpom.length(); i++) {
            JSONObject mObj = mHeatingSymtpom.getJSONObject(i);
            if (faultType.equals("value") && !mObj.getString("parameter_value_oper").isEmpty()) {
                float valueInSymptom = Float.parseFloat(mObj.getString("parameter_value"));
                float mFaultValue = Float.parseFloat(faultValue);
                if (mObj.getString("parameter_value_oper").equals("less")) {
                    if (mFaultValue < valueInSymptom) {
                        resultObj.put("symptom_id", mObj.getInt("symptom_id"));
                        resultObj.put("symptom_desc", "Temperatur less than " + mObj.getString("parameter_value") + "Degress");
                        resultObj.put("fault_location", mObj.getString("component_id"));
                    }
                } else if (mObj.getString("parameter_value_oper").equals("bigger")) {
                    if (mFaultValue > valueInSymptom) {
                        resultObj.put("symptom_id", mObj.getInt("symptom_id"));
                        resultObj.put("symptom_desc", "Temperatur bigger than " + mObj.getString("parameter_value") + "Degress");
                        resultObj.put("fault_location", mObj.getString("component_id"));
                    }
                } else {
                    // will not happend in theory
                }
            } else if (faultType.equals("changerate") && !mObj.getString("parameter_changerate_oper").isEmpty()) {
                float valueInSymptom = Float.parseFloat(mObj.getString("parameter_changerate"));
                float mFaultValue = Float.parseFloat(faultValue);
                if (mObj.getString("parameter_changerate_oper").equals("less")) {
                    if (mFaultValue < valueInSymptom) {
                        resultObj.put("symptom_id", mObj.getInt("symptom_id"));
                        resultObj.put("symptom_desc", "Temperatur changerate less than " + mObj.getString("parameter_changerate") + "s/Degress");
                        resultObj.put("fault_location", mObj.getString("component_id"));
                    }
                } else if (mObj.getString("parameter_changerate_oper").equals("bigger")) {
                    if (mFaultValue > valueInSymptom) {
                        System.out.println(mFaultValue);
                        System.out.println(valueInSymptom);
                        System.out.println(mObj.getString("parameter_changerate_oper"));
                        resultObj.put("symptom_id", mObj.getInt("symptom_id"));
                        resultObj.put("symptom_desc", "Temperatur changerate bigger than " + mObj.getString("parameter_changerate") + "s/Degress");
                        resultObj.put("fault_location", mObj.getString("component_id"));
                    }
                } else {
                    // will not happend in theory
                }
            } else if (faultType.equals("trend")) {
                // Todo: not implemented
            } else {
                resultObj.put("symptom_id", 15);
                resultObj.put("symptom_desc", "Temperatur Subsystem Defekt");
                resultObj.put("fault_location", "2");
            }
        }
        analysisProcedure.write("Compare Symptom Subsystem Heating Result dump: ");
        System.out.println(resultObj.toString());
        return resultObj;
    }

    private JSONObject symptomAirpressureAnalysis(String faultLocation, String faultType, String faultParam, String faultValue, String equipmentID) throws SQLException, NamingException {
        JSONObject resultObj = new JSONObject();
        JSONArray mAirpressureSymtpom = databaseSymptom.getSymptomAirpressure();
        String mParamNum = "";
        if (faultParam.equals("airpressure")) {
            mParamNum = "";
        } else if (faultParam.equals("airflowrate")) {
            mParamNum = "2";
        }
        for (int i = 0; i < mAirpressureSymtpom.length(); i++) {
            JSONObject mObj = mAirpressureSymtpom.getJSONObject(i);
            if (faultType.equals("value") && !mObj.getString("parameter" + mParamNum + "_value_oper").isEmpty()) {
                float valueInSymptom = Float.parseFloat(mObj.getString("parameter" + mParamNum + "_value"));
                float mFaultValue = Float.parseFloat(faultValue);
                if (mObj.getString("parameter" + mParamNum + "_value_oper").equals("less")) {
                    if (mFaultValue < valueInSymptom) {
                        resultObj.put("symptom_id", mObj.getInt("symptom_id"));
                        resultObj.put("symptom_desc", faultParam + " less than " + mObj.getString("parameter" + mParamNum + "_value"));
                        resultObj.put("fault_location", mObj.getString("component_id"));
                    }
                } else if (mObj.getString("parameter" + mParamNum + "_value_oper").equals("bigger")) {
                    if (mFaultValue > valueInSymptom) {
                        resultObj.put("symptom_id", mObj.getInt("symptom_id"));
                        resultObj.put("symptom_desc", faultParam + " bigger than " + mObj.getString("parameter" + mParamNum + "_value"));
                        resultObj.put("fault_location", mObj.getString("component_id"));
                    }
                } else {
                    // will not happend in theory
                }
            } else if (faultType.equals("changerate") && !mObj.getString("parameter" + mParamNum + "_changerate_oper").isEmpty()) {
                float valueInSymptom = Float.parseFloat(mObj.getString("parameter" + mParamNum + "_changerate"));
                float mFaultValue = Float.parseFloat(faultValue);
                if (mObj.getString("parameter" + mParamNum + "_changerate_oper").equals("less")) {
                    if (mFaultValue < valueInSymptom) {
                        resultObj.put("symptom_id", mObj.getInt("symptom_id"));
                        resultObj.put("symptom_desc", faultParam + " changerate less than " + mObj.getString("parameter" + mParamNum + "_changerate"));
                        resultObj.put("fault_location", mObj.getString("component_id"));
                    }
                } else if (mObj.getString("parameter" + mParamNum + "_changerate_oper").equals("bigger")) {
                    if (mFaultValue > valueInSymptom) {
                        resultObj.put("symptom_id", mObj.getInt("symptom_id"));
                        resultObj.put("symptom_desc", faultParam + " changerate bigger than " + mObj.getString("parameter" + mParamNum + "_changerate"));
                        resultObj.put("fault_location", mObj.getString("component_id"));
                    }
                } else {
                    // will not happend in theory
                }
            } else if (faultType.equals("trend")) {
                // Todo: not implemented
            } else {
                resultObj.put("symptom_id", 1);
                resultObj.put("symptom_desc", "Airpressure Subsystem Defekt");
                resultObj.put("fault_location", "23");
            }
        }
        analysisProcedure.write("Compare Symptom Subsystem Airpressure Result dump: ");
        System.out.println(resultObj.toString());
        return resultObj;
    }

    private JSONObject symptomPumpingAnalysis(String faultLocation, String faultType, String faultParam, String faultValue, String equipmentID) throws SQLException, NamingException {
        JSONObject resultObj = new JSONObject();
        JSONArray mPumpingSymtpom = databaseSymptom.getSymptomPumping();
        String mParamNum = "";
        if (faultParam.equals("waterpressure")) {
            mParamNum = "";
        } else if (faultParam.equals("waterflowrate")) {
            mParamNum = "2";
        }
        System.out.println(mParamNum);
        for (int i = 0; i < mPumpingSymtpom.length(); i++) {
            JSONObject mObj = mPumpingSymtpom.getJSONObject(i);
            if (faultType.equals("value") && !mObj.getString("parameter" + mParamNum + "_value_oper").isEmpty()) {
                float valueInSymptom = Float.parseFloat(mObj.getString("parameter" + mParamNum + "_value"));
                float mFaultValue = Float.parseFloat(faultValue);
                if (mObj.getString("parameter" + mParamNum + "_value_oper").equals("less")) {
                    if (mFaultValue < valueInSymptom) {
                        resultObj.put("symptom_id", mObj.getInt("symptom_id"));
                        resultObj.put("symptom_desc", faultParam + " less than " + mObj.getString("parameter" + mParamNum + "_value"));
                        resultObj.put("fault_location", mObj.getString("component_id"));
                    }
                } else if (mObj.getString("parameter" + mParamNum + "_value_oper").equals("bigger")) {
                    if (mFaultValue > valueInSymptom) {
                        resultObj.put("symptom_id", mObj.getInt("symptom_id"));
                        resultObj.put("symptom_desc", faultParam + " bigger than " + mObj.getString("parameter" + mParamNum + "_value"));
                        resultObj.put("fault_location", mObj.getString("component_id"));
                    }
                } else {
                    // will not happend in theory
                }
            } else if (faultType.equals("changerate") && !mObj.getString("parameter" + mParamNum + "_changerate_oper").isEmpty()) {
                float valueInSymptom = Float.parseFloat(mObj.getString("parameter" + mParamNum + "_changerate"));
                float mFaultValue = Float.parseFloat(faultValue);
                if (mObj.getString("parameter" + mParamNum + "_changerate_oper").equals("less")) {
                    if (mFaultValue < valueInSymptom) {
                        resultObj.put("symptom_id", mObj.getInt("symptom_id"));
                        resultObj.put("symptom_desc", faultParam + " changerate less than " + mObj.getString("parameter" + mParamNum + "_changerate"));
                        resultObj.put("fault_location", mObj.getString("component_id"));
                    }
                } else if (mObj.getString("parameter" + mParamNum + "_changerate_oper").equals("bigger")) {
                    if (mFaultValue > valueInSymptom) {
                        resultObj.put("symptom_id", mObj.getInt("symptom_id"));
                        resultObj.put("symptom_desc", faultParam + " changerate bigger than " + mObj.getString("parameter" + mParamNum + "_changerate"));
                        resultObj.put("fault_location", mObj.getString("component_id"));
                    }
                } else {
                    // will not happend in theory
                }
            } else if (faultType.equals("trend")) {
                // Todo: not implemented
            } else {
                resultObj.put("symptom_id", 1);
                resultObj.put("symptom_desc", "Pumping Subsystem Defekt");
                resultObj.put("fault_location", "12,13");
            }
        }
        analysisProcedure.write("Compare Symptom Subsystem Pumping Result dump: ");
        System.out.println(resultObj.toString());
        return resultObj;
    }

    private JSONObject symptomInflowAnalysis(String faultLocation, String faultType, String faultParam, String faultValue, String equipmentID) throws SQLException, NamingException {
        JSONObject resultObj = new JSONObject();
        JSONArray mInflowSymtpom = databaseSymptom.getSymptomInflow();
        for (int i = 0; i < mInflowSymtpom.length(); i++) {
            JSONObject mObj = mInflowSymtpom.getJSONObject(i);
            if (faultType.equals("value") && !mObj.getString("parameter_value_oper").isEmpty()) {
                float valueInSymptom = Float.parseFloat(mObj.getString("parameter_value"));
                float mFaultValue = Float.parseFloat(faultValue);
                if (mObj.getString("parameter_value_oper").equals("less")) {
                    if (mFaultValue < valueInSymptom) {
                        resultObj.put("symptom_id", mObj.getInt("symptom_id"));
                        resultObj.put("symptom_desc", "Waterlevel less than " + mObj.getString("parameter_value") + "L");
                        resultObj.put("fault_location", mObj.getString("component_id"));
                    }
                } else if (mObj.getString("parameter_value_oper").equals("bigger")) {
                    if (mFaultValue > valueInSymptom) {
                        resultObj.put("symptom_id", mObj.getInt("symptom_id"));
                        resultObj.put("symptom_desc", "Waterlevel bigger than " + mObj.getString("parameter_value") + "L");
                        resultObj.put("fault_location", mObj.getString("component_id"));
                    }
                } else {
                    // will not happend in theory
                }
            } else if (faultType.equals("changerate") && !mObj.getString("parameter_changerate_oper").isEmpty()) {
                float valueInSymptom = Float.parseFloat(mObj.getString("parameter_changerate"));
                float mFaultValue = Float.parseFloat(faultValue);
                if (mObj.getString("parameter_changerate_oper").equals("less")) {
                    if (mFaultValue < valueInSymptom) {
                        resultObj.put("symptom_id", mObj.getInt("symptom_id"));
                        resultObj.put("symptom_desc", "Waterlevel changerate less than " + mObj.getString("parameter_changerate") + "s/L");
                        resultObj.put("fault_location", mObj.getString("component_id"));
                    }
                } else if (mObj.getString("parameter_changerate_oper").equals("bigger")) {
                    if (mFaultValue > valueInSymptom) {
                        resultObj.put("symptom_id", mObj.getInt("symptom_id"));
                        resultObj.put("symptom_desc", "Waterlevel changerate bigger than " + mObj.getString("parameter_changerate") + "s/L");
                        resultObj.put("fault_location", mObj.getString("component_id"));
                    }
                } else {
                    // will not happend in theory
                }
            } else if (faultType.equals("trend")) {
                // Todo: not implemented
            } else {
                resultObj.put("symptom_id", 5);
                resultObj.put("symptom_desc", "Inflow Subsystem Defekt");
                resultObj.put("fault_location", "8,18");
            }
        }
        analysisProcedure.write("Compare Symptom Subsystem Inflow Result dump: ");
        System.out.println(resultObj.toString());
        return resultObj;
    }

}
