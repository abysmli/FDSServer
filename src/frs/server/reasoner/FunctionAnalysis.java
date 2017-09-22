/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.server.reasoner;

import frs.server.controller.AnalysisProcedureGenerator;
import frs.server.model.SystemDatabaseHandler;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author abysmli
 */
public class FunctionAnalysis {

    private final SystemDatabaseHandler databaseSystem = new SystemDatabaseHandler();
    private final AnalysisProcedureGenerator analysisProcedure;
    List<String> mTopFunctionsList = new ArrayList<>();
    Map<String, Boolean> mDefectFunction = new HashMap();
    JSONArray depthSearchAry = new JSONArray();
    JSONArray requirementDepthSearchAry = new JSONArray();
    boolean bFunctionAnalysisRerunFlag = false;
    boolean bRedundanzFlag = false;

    private JSONArray mComponentFunctionRel = new JSONArray();
    private JSONArray mFunctionsRel = new JSONArray();
    private JSONArray mBasicFunctions = new JSONArray();
    private JSONArray mSubFunctions = new JSONArray();
    private JSONArray mMainFunctions = new JSONArray();
    private JSONArray mSubRequirements = new JSONArray();
    private JSONArray mMainRequirements = new JSONArray();
    private JSONArray BasicFunctionsAvailability = new JSONArray();
    private JSONArray SubFunctionsAvailability = new JSONArray();
    private JSONArray MainFunctionsAvailability = new JSONArray();

    public FunctionAnalysis(AnalysisProcedureGenerator analysisProcedure) {
        this.analysisProcedure = analysisProcedure;
    }

    public JSONObject analysis(String mFaultLocation) throws SQLException, NamingException {
        mComponentFunctionRel = databaseSystem.getComponentFunctionRel();
        mFunctionsRel = databaseSystem.getFunctionsRel();
        mBasicFunctions = databaseSystem.getFunctions();
        mSubFunctions = databaseSystem.getSubfunctions();
        mMainFunctions = databaseSystem.getMainfunctions();
        mSubRequirements = databaseSystem.getSubRequirements();
        mMainRequirements = databaseSystem.getMainRequirements();
        analysisProcedure.faultLocalizationInfo.setFaultLocation(mFaultLocation);
        init();
        findTreeTopPoint(mFaultLocation);
        treeDepthSearchLoop(0);
        return generateResult();
    }

    private void init() {
        for (int i = 0; i < 26; i++) {
            JSONObject obj = new JSONObject();
            obj.put("function_id", i + 1);
            obj.put("availability", "true");
            BasicFunctionsAvailability.put(obj);
        }
        for (int i = 0; i < 7; i++) {
            JSONObject obj = new JSONObject();
            obj.put("sub_function_id", i + 1);
            obj.put("availability", "true");
            SubFunctionsAvailability.put(obj);
        }
        for (int i = 0; i < 4; i++) {
            JSONObject obj = new JSONObject();
            obj.put("main_function_id", i + 1);
            obj.put("availability", "true");
            MainFunctionsAvailability.put(obj);
        }
    }

    private void treeDepthSearchLoop(int index) {
        boolean resultFlag = true;
        switch (getTreeTopPoint(index)) {
            case "MF1":
                resultFlag = MF1(1, "Function");
                treeDepthSearchLoop(++index);
                break;
            case "MF2":
                resultFlag = MF2(1, "Function");
                treeDepthSearchLoop(++index);
                break;
            case "MF3":
                resultFlag = MF3(1, "Function");
                treeDepthSearchLoop(++index);
                break;
            case "MF4":
                resultFlag = MF4(1, "Function");
                treeDepthSearchLoop(++index);
                break;
            case "SF1":
                resultFlag = SF1(1, "Function");
                treeDepthSearchLoop(++index);
                break;
            case "SF2":
                resultFlag = SF2(1, "Function");
                treeDepthSearchLoop(++index);
                break;
            case "SF3":
                resultFlag = SF3(1, "Function");
                treeDepthSearchLoop(++index);
                break;
            case "SF4":
                resultFlag = SF4(1, "Function");
                treeDepthSearchLoop(++index);
                break;
            case "SF5":
                resultFlag = SF5(1, "Function");
                treeDepthSearchLoop(++index);
                break;
            case "SF6":
                resultFlag = SF6(1, "Function");
                treeDepthSearchLoop(++index);
                break;
            case "SF7":
                resultFlag = SF7(1, "Function");
                treeDepthSearchLoop(++index);
                break;
            default:
                if (!bFunctionAnalysisRerunFlag) {
                    analyseRequirement();
                }
                break;
        }
    }

    private String getTreeTopPoint(int index) {
        if (index < mTopFunctionsList.size()) {
            System.out.println();
            analysisProcedure.write("Depth First Search Recursive Time: " + (index + 1));
            return mTopFunctionsList.get(index);
        } else {
            return "";
        }
    }

    private void findTreeTopPoint(String mFaultLocation) {
        analysisProcedure.write("Function Analysis Step1: Find Top Main Function by searching Functions Relation Table");
        String[] mFaultLocations = mFaultLocation.split("-");
        mTopFunctionsList = new ArrayList<>();
        for (int i = 0; i < mFaultLocations.length; i++) {
            for (int j = 0; j < mComponentFunctionRel.length(); j++) {
                JSONObject mObj = mComponentFunctionRel.getJSONObject(j);
                if (Integer.parseInt(mFaultLocations[i]) == mObj.getInt("component_id")) {
                    String mBasicFunctionID = "BF" + mObj.getInt("function_id");
                    System.out.println("Fault Location: " + mFaultLocations[i] + " ===> FunctionID: " + "BF" + mObj.getInt("function_id"));
                    mDefectFunction.put(mBasicFunctionID, false);
                    for (int k = 0; k < mFunctionsRel.length(); k++) {
                        JSONObject mRel = mFunctionsRel.getJSONObject(k);
                        if (mRel.getInt(mBasicFunctionID) > 1) {
                            mTopFunctionsList.add(mRel.getString("function_name"));
                        }
                    }
                }
            }
        }

        System.out.println(mDefectFunction.toString());
        System.out.println(mTopFunctionsList.toString());
        for (int i = 0; i < mTopFunctionsList.size(); i++) {
            String mSubFunctionID = mTopFunctionsList.get(i);
            for (int k = 0; k < mFunctionsRel.length(); k++) {
                JSONObject mRel = mFunctionsRel.getJSONObject(k);
                if (mRel.getInt(mSubFunctionID) != 0) {
                    mTopFunctionsList.add(mRel.getString("function_name"));
                }
            }
        }
        System.out.println(mTopFunctionsList.toString());
    }

    private JSONObject generateResult() {
        analysisProcedure.functionAnalysisInfo.setFunctionAnalysis(depthSearchAry);
        analysisProcedure.functionAnalysisInfo.setRequirementAnalysis(requirementDepthSearchAry);
        JSONObject resultObj = new JSONObject();
        analysisProcedure.functionAnalysisInfo.setBasicFunctionAvailability(BasicFunctionsAvailability);
        analysisProcedure.functionAnalysisInfo.setSubFunctionAvailability(SubFunctionsAvailability);
        analysisProcedure.functionAnalysisInfo.setMainFunctionAvailability(MainFunctionsAvailability);
        resultObj.put("basic_functions", BasicFunctionsAvailability);
        resultObj.put("sub_functions", SubFunctionsAvailability);
        resultObj.put("main_functions", MainFunctionsAvailability);
        resultObj.put("Redundanz", bRedundanzFlag);
        System.out.println();
        analysisProcedure.write("Function Analysis Result dump:");
        System.out.println();
        analysisProcedure.write("Basic Functions Availability:");
        System.out.println(BasicFunctionsAvailability.toString());
        System.out.println();
        analysisProcedure.write("Sub Functions Availability:");
        System.out.println(SubFunctionsAvailability.toString());
        System.out.println();
        analysisProcedure.write("Main Functions Availability:");
        System.out.println(MainFunctionsAvailability.toString());
        System.out.println();
        analysisProcedure.write("redundanz: " + bRedundanzFlag);
        return resultObj;
    }

    private void analyseRequirement() {
        System.out.println();
        analysisProcedure.write("Requirement Analysis: ");
        if (MR1(1, "Requirement")) {
            System.out.println();
            analysisProcedure.write("System is runnable!");
        } else {
            System.out.println();
            analysisProcedure.write("System is not runnable!");
        }
        if (bFunctionAnalysisRerunFlag) {
            treeDepthSearchLoop(0);
        }
    }

    private boolean MF1(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "MF1");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "MF1 = SF1 * SF5 * SF6");
        stepInfo.put("function_info", getMainFunctionInfo(1));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": MF1 = SF1 * SF5 * SF6");
        boolean flag = SF1(step + 1, type) && SF5(step + 1, type) && SF6(step + 1, type);
        setFunctionAvailability("MF1", flag);
        String resultString = String.valueOf(flag);
        stepInfo = new JSONObject();
        stepInfo.put("id", "MF1");
        stepInfo.put("level", step);
        stepInfo.put("io", "out");
        stepInfo.put("result", resultString);
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "MF1 = SF1 * SF5 * SF6 = " + resultString);
        stepInfo.put("function_info", getMainFunctionInfo(1));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": MF1 = SF1 * SF5 * SF6 = " + resultString);
        return flag;
    }

    private boolean MF2(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "MF2");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "MF2 = SF3 * SF6");
        stepInfo.put("function_info", getMainFunctionInfo(2));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": MF2 = SF3 * SF6");
        boolean flag = SF3(step + 1, type) && SF6(step + 1, type);
        setFunctionAvailability("MF2", flag);
        String resultString = String.valueOf(flag);
        stepInfo = new JSONObject();
        stepInfo.put("id", "MF2");
        stepInfo.put("level", step);
        stepInfo.put("io", "out");
        stepInfo.put("result", resultString);
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "MF2 = SF3 * SF6 = " + resultString);
        stepInfo.put("function_info", getMainFunctionInfo(2));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": MF2 = SF3 * SF6 = " + resultString);
        return flag;
    }

    private boolean MF3(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "MF3");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "MF3 = SF3 * SF6");
        stepInfo.put("function_info", getMainFunctionInfo(3));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": MF3 = SF3 * SF6");
        boolean flag = SF3(step + 1, type) && SF6(step + 1, type);
        setFunctionAvailability("MF3", flag);
        String resultString = String.valueOf(flag);
        stepInfo = new JSONObject();
        stepInfo.put("id", "MF3");
        stepInfo.put("level", step);
        stepInfo.put("io", "out");
        stepInfo.put("result", resultString);
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "MF3 = SF3 * SF6 = " + resultString);
        stepInfo.put("function_info", getMainFunctionInfo(3));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": MF3 = SF3 * SF6 = " + resultString);
        return flag;
    }

    private boolean MF4(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "MF4");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "MF4 = SF6");
        stepInfo.put("function_info", getMainFunctionInfo(4));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": MF4 = SF6");
        boolean flag = SF6(step + 1, type);
        setFunctionAvailability("MF4", flag);
        String resultString = String.valueOf(flag);
        stepInfo = new JSONObject();
        stepInfo.put("id", "MF4");
        stepInfo.put("level", step);
        stepInfo.put("io", "out");
        stepInfo.put("result", resultString);
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "MF4 = SF6 = " + resultString);
        stepInfo.put("function_info", getMainFunctionInfo(4));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": MF4 = SF6 = " + resultString);
        return flag;
    }

    private boolean SF1(int step, String type) {
        if (checkFunctionDefect("SF1")) {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "SF1");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "unknown");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "SF1 = BF1 * BF4 * (BF5 OR BF7)");
            stepInfo.put("function_info", getSubFunctionInfo(1));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": SF1 = BF1 * BF4 * (BF5 OR BF7)");
            boolean flag = BF1(step + 1, type) && BF4(step + 1, type) && (BF5(step + 1, type) || BF7(step + 1, type));
            setFunctionAvailability("SF1", flag);
            String resultString = String.valueOf(flag);
            stepInfo = new JSONObject();
            stepInfo.put("id", "SF1");
            stepInfo.put("level", step);
            stepInfo.put("io", "out");
            stepInfo.put("result", resultString);
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "SF1 = BF1 * BF4 * (BF5 OR BF7) = " + resultString);
            stepInfo.put("function_info", getSubFunctionInfo(1));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": SF1 = BF1 * BF4 * (BF5 OR BF7) = " + resultString);
            return flag;
        } else {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "SF1");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "false");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "SF1 = false (Directly)");
            stepInfo.put("function_info", getSubFunctionInfo(1));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": SF1 = false (Directly)");
            return false;
        }
    }

    private boolean SF2(int step, String type) {
        if (checkFunctionDefect("SF2")) {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "SF2");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "unknown");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "SF2 = BF7 * BF11");
            stepInfo.put("function_info", getSubFunctionInfo(2));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": SF2 = BF7 * BF11");
            boolean flag = BF7(step + 1, type) && BF11(step + 1, type);
            setFunctionAvailability("SF2", flag);
            String resultString = String.valueOf(flag);
            stepInfo = new JSONObject();
            stepInfo.put("id", "SF2");
            stepInfo.put("level", step);
            stepInfo.put("io", "out");
            stepInfo.put("result", resultString);
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "SF2 = BF7 * BF11 = " + resultString);
            stepInfo.put("function_info", getSubFunctionInfo(2));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": SF2 = BF7 * BF11 = " + resultString);
            return flag;
        } else {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "SF2");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "false");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "SF2 = false (Directly)");
            stepInfo.put("function_info", getSubFunctionInfo(2));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": SF2 = false (Directly)");
            return false;
        }
    }

    private boolean SF3(int step, String type) {
        if (checkFunctionDefect("SF3")) {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "SF3");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "unknown");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "SF3 = BF3 * BF14 * BF21");
            stepInfo.put("function_info", getSubFunctionInfo(3));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": SF3 = BF3 * BF14 * BF21");
            boolean flag = BF3(step + 1, type) && BF14(step + 1, type) && BF21(step + 1, type);
            setFunctionAvailability("SF3", flag);
            String resultString = String.valueOf(flag);
            stepInfo = new JSONObject();
            stepInfo.put("id", "SF3");
            stepInfo.put("level", step);
            stepInfo.put("io", "out");
            stepInfo.put("result", resultString);
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "SF3 = BF3 * BF14 * BF21 = " + resultString);
            stepInfo.put("function_info", getSubFunctionInfo(3));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": SF3 = BF3 * BF14 * BF21 = " + resultString);
            return flag;
        } else {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "SF3");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "false");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "SF3 = false (Directly)");
            stepInfo.put("function_info", getSubFunctionInfo(3));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": SF3 = false (Directly)");
            return false;
        }
    }

    private boolean SF4(int step, String type) {
        if (checkFunctionDefect("SF4")) {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "SF4");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "unknown");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "SF4 = BF15 * BF16 * BF22 * BF23 * BF24");
            stepInfo.put("function_info", getSubFunctionInfo(4));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": SF4 = BF15 * BF16 * BF22 * BF23 * BF24");
            boolean flag = BF15(step + 1, type) && BF16(step + 1, type) && BF22(step + 1, type) && BF23(step + 1, type) && BF24(step + 1, type);
            setFunctionAvailability("SF4", flag);
            String resultString = String.valueOf(flag);
            stepInfo = new JSONObject();
            stepInfo.put("id", "SF4");
            stepInfo.put("level", step);
            stepInfo.put("io", "out");
            stepInfo.put("result", resultString);
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "SF4 = BF15 * BF16 * BF22 * BF23 * BF24 = " + resultString);
            stepInfo.put("function_info", getSubFunctionInfo(4));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": SF4 = BF15 * BF16 * BF22 * BF23 * BF24 = " + resultString);
            return flag;
        } else {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "SF4");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "false");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "SF4 = false (Directly)");
            stepInfo.put("function_info", getSubFunctionInfo(4));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": SF4 = false (Directly)");
            return false;
        }
    }

    private boolean SF5(int step, String type) {
        if (checkFunctionDefect("SF5")) {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "SF5");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "unknown");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "SF5 = BF2 * ((BF18 * BF20) OR BF21)");
            stepInfo.put("function_info", getSubFunctionInfo(5));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": SF5 = BF2 * ((BF18 * BF20) OR BF21)");
            boolean flag = BF2(step + 1, type) && ((BF18(step + 1, type) && BF20(step + 1, type)) || BF21(step + 1, type));
            setFunctionAvailability("SF5", flag);
            String resultString = String.valueOf(flag);
            stepInfo = new JSONObject();
            stepInfo.put("id", "SF5");
            stepInfo.put("level", step);
            stepInfo.put("io", "out");
            stepInfo.put("result", resultString);
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "SF5 = BF2 * ((BF18 * BF20) OR BF21) = " + resultString);
            stepInfo.put("function_info", getSubFunctionInfo(5));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": SF5 = BF2 * ((BF18 * BF20) OR BF21) = " + resultString);
            return flag;
        } else {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "SF5");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "false");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "SF5 = false (Directly)");
            stepInfo.put("function_info", getSubFunctionInfo(5));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": SF5 = false (Directly)");
            return false;
        }
    }

    private boolean SF6(int step, String type) {
        if (checkFunctionDefect("SF6")) {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "SF6");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "unknown");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "SF6 = BF6 * BF8 * BF9 * BF10 * BF12 * BF13 * BF25 * BF26");
            stepInfo.put("function_info", getSubFunctionInfo(6));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": SF6 = BF6 * BF8 * BF9 * BF10 * BF12 * BF13 * BF25 * BF26");
            boolean flag = BF6(step + 1, type) && BF8(step + 1, type) && BF9(step + 1, type) && BF10(step + 1, type) && BF12(step + 1, type) && BF13(step + 1, type) && BF25(step + 1, type) && BF26(step + 1, type);
            setFunctionAvailability("SF6", flag);
            String resultString = String.valueOf(flag);
            stepInfo = new JSONObject();
            stepInfo.put("id", "SF6");
            stepInfo.put("level", step);
            stepInfo.put("io", "out");
            stepInfo.put("result", resultString);
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "SF6 = BF6 * BF8 * BF9 * BF10 * BF12 * BF13 * BF25 * BF26 = " + resultString);
            stepInfo.put("function_info", getSubFunctionInfo(6));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": SF6 = BF6 * BF8 * BF9 * BF10 * BF12 * BF13 * BF25 * BF26 = " + resultString);
            return flag;
        } else {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "SF6");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "false");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "SF6 = false (Directly)");
            stepInfo.put("function_info", getSubFunctionInfo(6));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": SF6 = false (Directly)");
            return false;
        }
    }

    private boolean SF7(int step, String type) {
        if (checkFunctionDefect("SF7")) {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "SF7");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "unknown");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "SF7 = BF13 * BF17");
            stepInfo.put("function_info", getSubFunctionInfo(7));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": SF7 = BF13 * BF17");
            boolean flag = BF13(step + 1, type) && BF17(step + 1, type);
            setFunctionAvailability("SF7", flag);
            String resultString = String.valueOf(flag);
            stepInfo = new JSONObject();
            stepInfo.put("id", "SF7");
            stepInfo.put("level", step);
            stepInfo.put("io", "out");
            stepInfo.put("result", resultString);
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "SF7 = BF13 * BF17 = " + resultString);
            stepInfo.put("function_info", getSubFunctionInfo(7));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": SF7 = BF13 * BF17 = " + resultString);
            return flag;
        } else {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "SF7");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "false");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "SF7 = false (Directly)");
            stepInfo.put("function_info", getSubFunctionInfo(7));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": SF7 = false (Directly)");
            return false;
        }
    }

    private boolean BF1(int step, String type) {
        if (checkFunctionDefect("BF1")) {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF1");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "unknown");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF1 = BF5 OR BF7");
            stepInfo.put("function_info", getBasicFunctionInfo(1));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF1 = BF5 OR BF7");
            boolean flag = BF5(step + 1, type) || BF7(step + 1, type);
            setFunctionAvailability("BF1", flag);
            String resultString = String.valueOf(flag);
            stepInfo = new JSONObject();
            stepInfo.put("id", "BF1");
            stepInfo.put("level", step);
            stepInfo.put("io", "out");
            stepInfo.put("result", resultString);
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF1 = BF5 OR BF7 = " + resultString);
            stepInfo.put("function_info", getBasicFunctionInfo(1));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF1 = BF5 OR BF7 = " + resultString);
            return flag;
        } else {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF1");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "false");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF1 = false");
            stepInfo.put("function_info", getBasicFunctionInfo(1));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF1 = false");
            return false;
        }
    }

    private boolean BF2(int step, String type) {
        if (checkFunctionDefect("BF2")) {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF2");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "unknown");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF2 = BF18 * BF19");
            stepInfo.put("function_info", getBasicFunctionInfo(2));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF2 = BF18 * BF19");
            boolean flag = BF18(step + 1, type) && BF19(step + 1, type);
            setFunctionAvailability("BF2", flag);
            String resultString = String.valueOf(flag);
            stepInfo = new JSONObject();
            stepInfo.put("id", "BF2");
            stepInfo.put("level", step);
            stepInfo.put("io", "out");
            stepInfo.put("result", resultString);
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF2 = BF18 * BF19 = " + resultString);
            stepInfo.put("function_info", getBasicFunctionInfo(2));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF2 = BF18 * BF19 = " + resultString);
            return flag;
        } else {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF2");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "false");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF2 = false");
            stepInfo.put("function_info", getBasicFunctionInfo(2));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF2 = false");
            return false;
        }
    }

    private boolean BF3(int step, String type) {
        if (checkFunctionDefect("BF3")) {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF3");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "unknown");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF3 = BF21");
            stepInfo.put("function_info", getBasicFunctionInfo(3));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF3 = BF21");
            boolean flag = BF21(step + 1, type);
            setFunctionAvailability("BF3", flag);
            String resultString = String.valueOf(flag);
            stepInfo = new JSONObject();
            stepInfo.put("id", "BF3");
            stepInfo.put("level", step);
            stepInfo.put("io", "out");
            stepInfo.put("result", resultString);
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF3 = BF21 = " + resultString);
            stepInfo.put("function_info", getBasicFunctionInfo(3));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF3 = BF21 = " + resultString);
            return flag;
        } else {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF3");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "false");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF3 = false");
            stepInfo.put("function_info", getBasicFunctionInfo(3));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF3 = false");
            return false;
        }
    }

    private boolean BF4(int step, String type) {
        if (checkFunctionDefect("BF4")) {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF4");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "unknown");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF4 = BF1");
            stepInfo.put("function_info", getBasicFunctionInfo(4));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF4 = BF1");
            boolean flag = BF1(step + 1, type);
            setFunctionAvailability("BF4", flag);
            String resultString = String.valueOf(flag);
            stepInfo = new JSONObject();
            stepInfo.put("id", "BF4");
            stepInfo.put("level", step);
            stepInfo.put("io", "out");
            stepInfo.put("result", resultString);
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF4 = BF1 = " + resultString);
            stepInfo.put("function_info", getBasicFunctionInfo(4));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF4 = BF1 = " + resultString);
            return flag;
        } else {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF4");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "false");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF4 = false");
            stepInfo.put("function_info", getBasicFunctionInfo(4));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF4 = false");
            return false;
        }
    }

    private boolean BF5(int step, String type) {
        boolean result = checkFunctionDefect("BF5");
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "BF5");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", String.valueOf(result));
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "BF5 = " + String.valueOf(result));
        stepInfo.put("function_info", getBasicFunctionInfo(5));
        setFunctionAvailability("BF5", result);
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        if (result) {
            analysisProcedure.write(type + " Tree Level " + step + ": BF5 = true");
        } else {
            analysisProcedure.write("BF5 is defected. Use BF7 as redundanz.");
            analysisProcedure.write(type + " Tree Level " + step + ": BF5(BF7) = true");
            bRedundanzFlag = true;
        }
        return true;
    }

    private boolean BF6(int step, String type) {
        if (checkFunctionDefect("BF6")) {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF6");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "unknown");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF6 = BF10");
            stepInfo.put("function_info", getBasicFunctionInfo(6));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF6 = BF10");
            boolean flag = BF10(step + 1, type);
            setFunctionAvailability("BF6", flag);
            String resultString = String.valueOf(flag);
            stepInfo = new JSONObject();
            stepInfo.put("id", "BF6");
            stepInfo.put("level", step);
            stepInfo.put("io", "out");
            stepInfo.put("result", resultString);
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF6 = BF10 = " + resultString);
            stepInfo.put("function_info", getBasicFunctionInfo(6));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF6 = BF10 = " + resultString);
            return flag;
        } else {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF6");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "false");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF6 = false");
            stepInfo.put("function_info", getBasicFunctionInfo(6));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF6 = false");
            return false;
        }
    }

    private boolean BF7(int step, String type) {
        boolean result = checkFunctionDefect("BF7");
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "BF7");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", String.valueOf(result));
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "BF7 = " + String.valueOf(result));
        stepInfo.put("function_info", getBasicFunctionInfo(7));
        setFunctionAvailability("BF7", result);
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": BF7 = " + String.valueOf(result));
        return result;
    }

    private boolean BF8(int step, String type) {
        boolean result = checkFunctionDefect("BF8");
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "BF8");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", String.valueOf(result));
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "BF8 = " + String.valueOf(result));
        stepInfo.put("function_info", getBasicFunctionInfo(8));
        setFunctionAvailability("BF8", result);
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": BF8 = " + String.valueOf(result));
        return result;
    }

    private boolean BF9(int step, String type) {
        if (checkFunctionDefect("BF9")) {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF9");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "unknown");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF9 = BF10");
            stepInfo.put("function_info", getBasicFunctionInfo(9));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF9 = BF10");
            boolean flag = BF10(step + 1, type);
            setFunctionAvailability("BF9", flag);
            String resultString = String.valueOf(flag);
            stepInfo = new JSONObject();
            stepInfo.put("id", "BF9");
            stepInfo.put("level", step);
            stepInfo.put("io", "out");
            stepInfo.put("result", resultString);
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF9 = BF10 = " + resultString);
            stepInfo.put("function_info", getBasicFunctionInfo(9));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF9 = BF10 = " + resultString);
            return flag;
        } else {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF9");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "false");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF9 = false");
            stepInfo.put("function_info", getBasicFunctionInfo(9));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF9 = false");
            return false;
        }
    }

    private boolean BF10(int step, String type) {
        if (checkFunctionDefect("BF10")) {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF10");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "unknown");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF10 = BF8 * BF13 * BF25 * BF26");
            stepInfo.put("function_info", getBasicFunctionInfo(10));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF10 = BF8 * BF13 * BF25 * BF26");
            boolean flag = BF8(step + 1, type) && BF13(step + 1, type) && BF25(step + 1, type) && BF26(step + 1, type);
            setFunctionAvailability("BF10", flag);
            String resultString = String.valueOf(flag);
            stepInfo = new JSONObject();
            stepInfo.put("id", "BF10");
            stepInfo.put("level", step);
            stepInfo.put("io", "out");
            stepInfo.put("result", resultString);
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF10 = BF8 * BF13 * BF25 * BF26 = " + resultString);
            stepInfo.put("function_info", getBasicFunctionInfo(10));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF10 = BF8 * BF13 * BF25 * BF26 = " + resultString);
            return flag;
        } else {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF10");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "false");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF10 = false");
            stepInfo.put("function_info", getBasicFunctionInfo(10));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF10 = false");
            return false;
        }
    }

    private boolean BF11(int step, String type) {
        if (checkFunctionDefect("BF11")) {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF11");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "unknown");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF11 = BF5 * BF7");
            stepInfo.put("function_info", getBasicFunctionInfo(11));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF11 = BF5 * BF7");
            boolean flag = BF5(step + 1, type) && BF7(step + 1, type);
            setFunctionAvailability("BF11", flag);
            String resultString = String.valueOf(flag);
            stepInfo = new JSONObject();
            stepInfo.put("id", "BF11");
            stepInfo.put("level", step);
            stepInfo.put("io", "out");
            stepInfo.put("result", resultString);
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF11 = BF5 * BF7 = " + resultString);
            stepInfo.put("function_info", getBasicFunctionInfo(11));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF11 = BF5 * BF7 = " + resultString);
            return flag;
        } else {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF11");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "false");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF11 = false");
            stepInfo.put("function_info", getBasicFunctionInfo(11));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF11 = false");
            return false;
        }
    }

    private boolean BF12(int step, String type) {
        if (checkFunctionDefect("BF12")) {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF12");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "unknown");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF12 = BF10");
            stepInfo.put("function_info", getBasicFunctionInfo(12));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF12 = BF10");
            boolean flag = BF10(step + 1, type);
            setFunctionAvailability("BF12", flag);
            String resultString = String.valueOf(flag);
            stepInfo = new JSONObject();
            stepInfo.put("id", "BF12");
            stepInfo.put("level", step);
            stepInfo.put("io", "out");
            stepInfo.put("result", resultString);
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF12 = BF10 = " + resultString);
            stepInfo.put("function_info", getBasicFunctionInfo(12));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF12 = BF10 = " + resultString);
            return flag;
        } else {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF12");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "false");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF12 = false");
            stepInfo.put("function_info", getBasicFunctionInfo(12));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF12 = false");
            return false;
        }
    }

    private boolean BF13(int step, String type) {
        boolean result = checkFunctionDefect("BF13");
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "BF13");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", String.valueOf(result));
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "BF13 = " + String.valueOf(result));
        stepInfo.put("function_info", getBasicFunctionInfo(13));
        setFunctionAvailability("BF13", result);
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": BF13 = " + String.valueOf(result));
        return result;
    }

    private boolean BF14(int step, String type) {
        if (checkFunctionDefect("BF14")) {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF14");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "unknown");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF14 = BF3 * BF4");
            stepInfo.put("function_info", getBasicFunctionInfo(14));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF14 = BF3 * BF4");
            boolean flag = BF3(step + 1, type) && BF4(step + 1, type);
            setFunctionAvailability("BF14", flag);
            String resultString = String.valueOf(flag);
            stepInfo = new JSONObject();
            stepInfo.put("id", "BF14");
            stepInfo.put("level", step);
            stepInfo.put("io", "out");
            stepInfo.put("result", resultString);
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF14 = BF3 * BF4 = " + resultString);
            stepInfo.put("function_info", getBasicFunctionInfo(14));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF14 = BF3 * BF4 = " + resultString);
            return flag;
        } else {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF14");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "false");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF14 = false");
            stepInfo.put("function_info", getBasicFunctionInfo(14));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF14 = false");
            return false;
        }
    }

    private boolean BF15(int step, String type) {
        boolean result = checkFunctionDefect("BF15");
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "BF15");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", String.valueOf(result));
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "BF15 = " + String.valueOf(result));
        stepInfo.put("function_info", getBasicFunctionInfo(15));
        setFunctionAvailability("BF15", result);
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": BF15 = " + String.valueOf(result));
        return result;
    }

    private boolean BF16(int step, String type) {
        boolean result = checkFunctionDefect("BF16");
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "BF16");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", String.valueOf(result));
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "BF16 = " + String.valueOf(result));
        stepInfo.put("function_info", getBasicFunctionInfo(16));
        setFunctionAvailability("BF16", result);
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": BF16 = " + String.valueOf(result));
        return result;
    }

    private boolean BF17(int step, String type) {
        if (checkFunctionDefect("BF17")) {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF17");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "unknown");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF17 = BF13");
            stepInfo.put("function_info", getBasicFunctionInfo(17));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF17 = BF13");
            boolean flag = BF13(step + 1, type);
            setFunctionAvailability("BF17", flag);
            String resultString = String.valueOf(flag);
            stepInfo = new JSONObject();
            stepInfo.put("id", "BF17");
            stepInfo.put("level", step);
            stepInfo.put("io", "out");
            stepInfo.put("result", resultString);
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF17 = BF13 = " + resultString);
            stepInfo.put("function_info", getBasicFunctionInfo(17));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF17 = BF13 = " + resultString);
            return flag;
        } else {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF17");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "false");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF17 = false");
            stepInfo.put("function_info", getBasicFunctionInfo(17));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF17 = false");
            return false;
        }
    }

    private boolean BF18(int step, String type) {
        boolean result = checkFunctionDefect("BF18");
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "BF18");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", String.valueOf(result));
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "BF18 = " + String.valueOf(result));
        stepInfo.put("function_info", getBasicFunctionInfo(18));
        setFunctionAvailability("BF18", result);
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": BF18 = " + String.valueOf(result));
        return result;
    }

    private boolean BF19(int step, String type) {
        boolean result = checkFunctionDefect("BF19");
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "BF19");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", String.valueOf(result));
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "BF19 = " + String.valueOf(result));
        stepInfo.put("function_info", getBasicFunctionInfo(19));
        setFunctionAvailability("BF19", result);
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": BF19 = " + String.valueOf(result));
        return result;
    }

    private boolean BF20(int step, String type) {
        boolean result = checkFunctionDefect("BF20");
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "BF20");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", String.valueOf(result));
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "BF20 = " + String.valueOf(result));
        stepInfo.put("function_info", getBasicFunctionInfo(20));
        setFunctionAvailability("BF20", result);
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": BF20 = " + String.valueOf(result));
        return result;
    }

    private boolean BF21(int step, String type) {
        boolean result = checkFunctionDefect("BF21");
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "BF21");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", String.valueOf(result));
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "BF21 = " + String.valueOf(result));
        stepInfo.put("function_info", getBasicFunctionInfo(21));
        setFunctionAvailability("BF21", result);
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": BF21 = " + String.valueOf(result));
        return result;
    }

    private boolean BF22(int step, String type) {
        if (checkFunctionDefect("BF22")) {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF22");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "unknown");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF22 = BF15 * BF16");
            stepInfo.put("function_info", getBasicFunctionInfo(22));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF22 = BF15 * BF16");
            boolean flag = BF15(step + 1, type) && BF16(step + 1, type);
            setFunctionAvailability("BF22", flag);
            String resultString = String.valueOf(flag);
            stepInfo = new JSONObject();
            stepInfo.put("id", "BF22");
            stepInfo.put("level", step);
            stepInfo.put("io", "out");
            stepInfo.put("result", resultString);
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF22 = BF15 * BF16 = " + resultString);
            stepInfo.put("function_info", getBasicFunctionInfo(22));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF22 = BF15 * BF16 = " + resultString);
            return flag;
        } else {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF22");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "false");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF22 = false");
            stepInfo.put("function_info", getBasicFunctionInfo(22));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF22 = false");
            return false;
        }
    }

    private boolean BF23(int step, String type) {
        if (checkFunctionDefect("BF23")) {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF23");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "unknown");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF23 = BF22");
            stepInfo.put("function_info", getBasicFunctionInfo(23));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF23 = BF22");
            boolean flag = BF22(step + 1, type);
            setFunctionAvailability("BF23", flag);
            String resultString = String.valueOf(flag);
            stepInfo = new JSONObject();
            stepInfo.put("id", "BF23");
            stepInfo.put("level", step);
            stepInfo.put("io", "out");
            stepInfo.put("result", resultString);
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF23 = BF22 = " + resultString);
            stepInfo.put("function_info", getBasicFunctionInfo(23));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF23 = BF22 = " + resultString);
            return flag;
        } else {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF23");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "false");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF23 = false");
            stepInfo.put("function_info", getBasicFunctionInfo(23));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF23 = false");
            return false;
        }
    }

    private boolean BF24(int step, String type) {
        if (checkFunctionDefect("BF24")) {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF24");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "unknown");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF24 = BF22");
            stepInfo.put("function_info", getBasicFunctionInfo(24));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF24 = BF22");
            boolean flag = BF22(step + 1, type);
            setFunctionAvailability("BF24", flag);
            String resultString = String.valueOf(flag);
            stepInfo = new JSONObject();
            stepInfo.put("id", "BF24");
            stepInfo.put("level", step);
            stepInfo.put("io", "out");
            stepInfo.put("result", resultString);
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF24 = BF22 = " + resultString);
            stepInfo.put("function_info", getBasicFunctionInfo(24));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF24 = BF22 = " + resultString);
            return flag;
        } else {
            JSONObject stepInfo = new JSONObject();
            stepInfo.put("id", "BF24");
            stepInfo.put("level", step);
            stepInfo.put("io", "in");
            stepInfo.put("result", "false");
            stepInfo.put("analysis_type", type);
            stepInfo.put("rule", "BF24 = false");
            stepInfo.put("function_info", getBasicFunctionInfo(24));
            if (type.equals("Function")) {
                depthSearchAry.put(stepInfo);
            } else {
                requirementDepthSearchAry.put(stepInfo);
            }
            analysisProcedure.write(type + " Tree Level " + step + ": BF24 = false");
            return false;
        }
    }

    private boolean BF25(int step, String type) {
        boolean result = checkFunctionDefect("BF25");
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "BF25");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", String.valueOf(result));
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "BF25 = " + String.valueOf(result));
        stepInfo.put("function_info", getBasicFunctionInfo(25));
        setFunctionAvailability("BF25", result);
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": BF25 = " + String.valueOf(result));
        return result;
    }

    private boolean BF26(int step, String type) {
        boolean result = checkFunctionDefect("BF26");
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "BF26");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", String.valueOf(result));
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "BF26 = " + String.valueOf(result));
        stepInfo.put("function_info", getBasicFunctionInfo(26));
        setFunctionAvailability("BF26", result);
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": BF26 = " + String.valueOf(result));
        return result;
    }

    private boolean MR1(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "MR1");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "MR1 = (SR2 * SR3) * (SR1 OR (SR2 * SR3))");
        stepInfo.put("function_info", getMainRequirementInfo(1));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": MR1 = (SR2 * SR3) * (SR1 OR (SR2 * SR3))");
        boolean flag = (SR2(step + 1, type) && SR3(step + 1, type)) && (SR1(step + 1, type) || (SR2(step + 1, type) && SR3(step + 1, type)));
        String resultString = String.valueOf(flag);
        stepInfo = new JSONObject();
        stepInfo.put("id", "MR1");
        stepInfo.put("level", step);
        stepInfo.put("io", "out");
        stepInfo.put("result", resultString);
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "MR1 = (SR2 * SR3) * (SR1 OR (SR2 * SR3)) = " + resultString);
        stepInfo.put("function_info", getMainRequirementInfo(1));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": MR1 = (SR2 * SR3) * (SR1 OR (SR2 * SR3)) = " + resultString);
        return flag;
    }

    private boolean SR1(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "SR1");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "SR1 = SF2");
        stepInfo.put("function_info", getSubRequirementInfo(1));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": SR1 = SF2");
        boolean flag = SF2(step + 1, type);
        String resultString = String.valueOf(flag);
        stepInfo = new JSONObject();
        stepInfo.put("id", "SR1");
        stepInfo.put("level", step);
        stepInfo.put("io", "out");
        stepInfo.put("result", resultString);
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "SR1 = SF2 = " + resultString);
        stepInfo.put("function_info", getSubRequirementInfo(1));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": SR1 = SF2 = " + resultString);
        if (flag) {
            analysisProcedure.write(type + " of [Temperatur Ueberwachen] is fulfilled!");
        } else {
            bFunctionAnalysisRerunFlag = true;
            analysisProcedure.write(type + " of [Temperatur Ueberwachen] is not fulfilled!");
            mDefectFunction.put("SF1", false);
            analysisProcedure.write(type + " SR1's Consequence SF1 is setted 'false'");
        }
        return flag;
    }

    private boolean SR2(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "SR2");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "SR2 = SF7");
        stepInfo.put("function_info", getSubRequirementInfo(2));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": SR2 = SF7");
        boolean flag = SF7(step + 1, type);
        String resultString = String.valueOf(flag);
        stepInfo = new JSONObject();
        stepInfo.put("id", "SR2");
        stepInfo.put("level", step);
        stepInfo.put("io", "out");
        stepInfo.put("result", resultString);
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "SR2 = SF7 = " + resultString);
        stepInfo.put("function_info", getSubRequirementInfo(2));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": SR2 = SF7 = " + resultString);
        if (flag) {
            analysisProcedure.write(type + " of [Wasserdruck Ueberwachen] is fulfilled!");
        } else {
            bFunctionAnalysisRerunFlag = true;
            analysisProcedure.write(type + " of [Wasserdruck Ueberwachen] is not fulfilled!");
            mDefectFunction.put("SF6", false);
            analysisProcedure.write(type + " SR2's Consequence SF6 is setted 'false'");
        }
        return flag;
    }

    private boolean SR3(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "SR3");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "SR3 = SF5");
        stepInfo.put("function_info", getSubRequirementInfo(3));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": SR3 = SF5");
        boolean flag = SF5(step + 1, type);
        String resultString = String.valueOf(flag);
        stepInfo = new JSONObject();
        stepInfo.put("id", "SR3");
        stepInfo.put("level", step);
        stepInfo.put("io", "out");
        stepInfo.put("result", resultString);
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "SR3 = SF5 = " + resultString);
        stepInfo.put("function_info", getSubRequirementInfo(3));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": SR3 = SF5 = " + resultString);
        if (flag) {
            analysisProcedure.write(type + " of [Fuellstand Ueberwachen] is fulfilled!");
        } else {
            bFunctionAnalysisRerunFlag = true;
            analysisProcedure.write(type + " of [Fuellstand Ueberwachen] is not fulfilled!");
            mDefectFunction.put("SF3", false);
            analysisProcedure.write(type + " SR3's Consequence SF3 is setted 'false'");
        }
        return flag;
    }

    private JSONObject getMainFunctionInfo(int i) {
        JSONObject obj = mMainFunctions.getJSONObject(i - 1);
        obj.put("desc", obj.getString("mainfunction_desc"));
        return obj;
    }

    private JSONObject getSubFunctionInfo(int i) {
        JSONObject obj = mSubFunctions.getJSONObject(i - 1);
        obj.put("desc", obj.getString("subfunction_desc"));
        return obj;
    }

    private JSONObject getBasicFunctionInfo(int i) {
        JSONObject obj = mBasicFunctions.getJSONObject(i - 1);
        obj.put("desc", obj.getString("function_desc"));
        return obj;
    }

    private JSONObject getMainRequirementInfo(int i) {
        JSONObject obj = mMainRequirements.getJSONObject(i - 1);
        obj.put("desc", obj.getString("main_requirement_name"));
        return obj;
    }

    private JSONObject getSubRequirementInfo(int i) {
        JSONObject obj = mSubRequirements.getJSONObject(i - 1);
        obj.put("desc", obj.getString("sub_requirement_name"));
        return obj;
    }

    private boolean checkFunctionDefect(String key) {
        return !mDefectFunction.containsKey(key);
    }

    private void setFunctionAvailability(String key, boolean availability) {
        int id = Integer.valueOf(key.substring(2));
        switch (key.substring(0, 2)) {
            case "MF":
                for (int i = 0; i < MainFunctionsAvailability.length(); i++) {
                    if (MainFunctionsAvailability.getJSONObject(i).getInt("main_function_id") == id) {
                        MainFunctionsAvailability.getJSONObject(i).put("availability", String.valueOf(availability));
                    }
                }
                break;
            case "SF":
                for (int i = 0; i < SubFunctionsAvailability.length(); i++) {
                    if (SubFunctionsAvailability.getJSONObject(i).getInt("sub_function_id") == id) {
                        SubFunctionsAvailability.getJSONObject(i).put("availability", String.valueOf(availability));
                    }
                }
                break;
            case "BF":
                for (int i = 0; i < BasicFunctionsAvailability.length(); i++) {
                    if (BasicFunctionsAvailability.getJSONObject(i).getInt("function_id") == id) {
                        BasicFunctionsAvailability.getJSONObject(i).put("availability", String.valueOf(availability));
                    }
                }
                break;
            default:
                break;
        }
    }
}
