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
import java.util.List;
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
    JSONArray depthSearchAry = new JSONArray();
    JSONArray requirementDepthSearchAry = new JSONArray();
    boolean SF2Flag = true;

    private JSONArray mBasicFunctions = new JSONArray();
    private JSONArray mSubFunctions = new JSONArray();
    private JSONArray mMainFunctions = new JSONArray();
    private JSONArray mBasicRequirements = new JSONArray();
    private JSONArray mSubRequirements = new JSONArray();
    private JSONArray mMainRequirements = new JSONArray();

    public FunctionAnalysis(AnalysisProcedureGenerator analysisProcedure) {
        this.analysisProcedure = analysisProcedure;
    }

    public JSONObject analysis(String mFaultLocation) throws SQLException, NamingException {
        mBasicFunctions = databaseSystem.getFunctions();
        mSubFunctions = databaseSystem.getSubfunctions();
        mMainFunctions = databaseSystem.getMainfunctions();
        mBasicRequirements = databaseSystem.getRequirements();
        mSubRequirements = databaseSystem.getSubRequirements();
        mMainRequirements = databaseSystem.getMainRequirements();

        analysisProcedure.faultLocalizationInfo.setFaultLocation(mFaultLocation);
        findTreeTopPoint();
        treeDepthFirstSearchLoop(0);
        return generateResult();
    }

    private void treeDepthFirstSearchLoop(int index) {
        boolean resultFlag = true;
        switch (getTreeTopPoint(index)) {
            case "MF1":
                resultFlag = MF1(1, "Function");
                treeDepthFirstSearchLoop(++index);
                break;
            case "MF2":
                resultFlag = MF2(1, "Function");
                treeDepthFirstSearchLoop(++index);
                break;
            case "MF3":
                resultFlag = MF3(1, "Function");
                treeDepthFirstSearchLoop(++index);
                break;
            case "MF4":
                resultFlag = MF4(1, "Function");
                treeDepthFirstSearchLoop(++index);
                break;
            case "SF1":
                resultFlag = SF1(1, "Function");
                treeDepthFirstSearchLoop(++index);
                break;
            case "SF2":
                resultFlag = SF2(1, "Function");
                treeDepthFirstSearchLoop(++index);
                break;
            case "SF3":
                resultFlag = SF3(1, "Function");
                treeDepthFirstSearchLoop(++index);
                break;
            case "SF4":
                resultFlag = SF4(1, "Function");
                treeDepthFirstSearchLoop(++index);
                break;
            case "SF5":
                resultFlag = SF5(1, "Function");
                treeDepthFirstSearchLoop(++index);
                break;
            case "SF6":
                resultFlag = SF6(1, "Function");
                treeDepthFirstSearchLoop(++index);
                break;
            case "SF7":
                resultFlag = SF7(1, "Function");
                treeDepthFirstSearchLoop(++index);
                break;
            default:
                analyseRequirement();
                break;
        }
    }

    private String getTreeTopPoint(int index) {
        if (index < mTopFunctionsList.size()) {
            analysisProcedure.write("\nDepth First Search Recursive Time: " + (index + 1));
            return mTopFunctionsList.get(index);
        } else {
            return "";
        }
    }

    private void findTreeTopPoint() {
        analysisProcedure.write("Function Analysis Step1: Find Top Main Function by searching Functions Relation Table");
        mTopFunctionsList = new ArrayList<>();
        mTopFunctionsList.add("MF1");
        mTopFunctionsList.add("SF1");
        mTopFunctionsList.add("SF2");
    }

    private JSONObject generateResult() {
        analysisProcedure.functionAnalysisInfo.setFunctionAnalysis(depthSearchAry);
        analysisProcedure.functionAnalysisInfo.setRequirementAnalysis(requirementDepthSearchAry);
        JSONObject resultObj = new JSONObject();
        JSONArray BasicFunctions = new JSONArray();
        JSONArray SubFunctions = new JSONArray();
        JSONArray MainFunctions = new JSONArray();
        for (int i = 0; i < 21; i++) {
            JSONObject obj = new JSONObject();
            obj.put("function_id", i + 1);
            if (true) {
                if (i == 5) {
                    obj.put("availability", "false");
                } else {
                    obj.put("availability", "true");
                }
            }
            BasicFunctions.put(obj);
        }
        for (int i = 0; i < 9; i++) {
            JSONObject obj = new JSONObject();
            obj.put("sub_function_id", i + 1);
            obj.put("availability", "true");
            SubFunctions.put(obj);
        }
        for (int i = 0; i < 3; i++) {
            JSONObject obj = new JSONObject();
            obj.put("main_function_id", i + 1);
            obj.put("availability", "true");
            MainFunctions.put(obj);
        }
        analysisProcedure.functionAnalysisInfo.setBasicFunctionAvailability(BasicFunctions);
        analysisProcedure.functionAnalysisInfo.setSubFunctionAvailability(SubFunctions);
        analysisProcedure.functionAnalysisInfo.setMainFunctionAvailability(MainFunctions);
        resultObj.put("basic_functions", BasicFunctions);
        resultObj.put("sub_functions", SubFunctions);
        resultObj.put("main_functions", MainFunctions);
        analysisProcedure.write("\nFunction Analysis Result dump:");
        analysisProcedure.write("\nBasic Functions Availability:");
        analysisProcedure.write(BasicFunctions.toString());
        analysisProcedure.write("\nSub Functions Availability:");
        analysisProcedure.write(SubFunctions.toString());
        analysisProcedure.write("\nMain Functions Availability:");
        analysisProcedure.write(MainFunctions.toString());
        return resultObj;
    }

    private void analyseRequirement() {
        analysisProcedure.write("\nRequirement Analysis: ");
        if (MR1(1, "Requirement")) {
            analysisProcedure.write("\nSystem is runnable!");
        } else {
            analysisProcedure.write("\nSystem is not runnable!");
        }
    }

    private boolean MF1(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "MF1");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "MF1 = SF1 * SF2");
        stepInfo.put("function_info", getMainFunctionInfo(1));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": MF1 = SF1 * SF2");
        boolean flag = SF1(step + 1, type) && SF2(step + 1, type);
        String resultString = String.valueOf(flag);
        stepInfo = new JSONObject();
        stepInfo.put("id", "MF1");
        stepInfo.put("level", step);
        stepInfo.put("io", "out");
        stepInfo.put("result", resultString);
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "MF1 = SF1 * SF2 = " + resultString);
        stepInfo.put("function_info", getMainFunctionInfo(1));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": MF1 = SF1 * SF2 = " + resultString);
        return flag;
    }

    private boolean MF2(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "MF2");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "MF2 = SF3 * SF5 * SF6 * SF7");
        stepInfo.put("function_info", getMainFunctionInfo(2));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": MF2 = SF3 * SF5 * SF6 * SF7");
        boolean flag = SF3(step + 1, type) && SF5(step + 1, type) && SF6(step + 1, type) && SF7(step + 1, type);
        String resultString = String.valueOf(flag);
        stepInfo = new JSONObject();
        stepInfo.put("id", "MF2");
        stepInfo.put("level", step);
        stepInfo.put("io", "out");
        stepInfo.put("result", resultString);
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "MF2 = SF3 * SF5 * SF6 * SF7 = " + resultString);
        stepInfo.put("function_info", getMainFunctionInfo(2));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": MF2 = SF3 * SF5 * SF6 * SF7 = " + resultString);
        return flag;
    }

    private boolean MF3(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "MF3");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "MF3 = SF3 * SF5");
        stepInfo.put("function_info", getMainFunctionInfo(3));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": MF3 = SF3 * SF5");
        boolean flag = SF3(step + 1, type) && SF5(step + 1, type);
        String resultString = String.valueOf(flag);
        stepInfo = new JSONObject();
        stepInfo.put("id", "MF3");
        stepInfo.put("level", step);
        stepInfo.put("io", "out");
        stepInfo.put("result", resultString);
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "MF3 = SF3 * SF5 = " + resultString);
        stepInfo.put("function_info", getMainFunctionInfo(3));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": MF3 = SF3 * SF5 = " + resultString);
        return flag;
    }

    private boolean MF4(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "MF4");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "MF4 = SF6 * SF7");
        stepInfo.put("function_info", getMainFunctionInfo(4));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": MF4 = SF6 * SF7");
        boolean flag = SF6(step + 1, type) && SF7(step + 1, type);
        String resultString = String.valueOf(flag);
        stepInfo = new JSONObject();
        stepInfo.put("id", "MF4");
        stepInfo.put("level", step);
        stepInfo.put("io", "out");
        stepInfo.put("result", resultString);
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "MF4 = SF6 * SF7 = " + resultString);
        stepInfo.put("function_info", getMainFunctionInfo(4));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": MF4 = SF6 * SF7 = " + resultString);
        return flag;
    }

    private boolean SF1(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "SF1");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "SF1 = F1 * F4 * (F5 OR F7)");
        stepInfo.put("function_info", getSubFunctionInfo(1));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": SF1 = F1 * F4 * (F5 OR F7)");
        boolean flag = F1(step + 1, type) && F4(step + 1, type) && (F5(step + 1, type) || F7(step + 1, type));
        String resultString = String.valueOf(flag);
        stepInfo = new JSONObject();
        stepInfo.put("id", "SF1");
        stepInfo.put("level", step);
        stepInfo.put("io", "out");
        stepInfo.put("result", resultString);
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "SF1 = F1 * F4 * (F5 OR F7) = " + resultString);
        stepInfo.put("function_info", getSubFunctionInfo(1));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": SF1 = F1 * F4 * (F5 OR F7) = " + resultString);
        return flag;
    }

    private boolean SF2(int step, String type) {
        boolean flag = true;
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "SF2");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "SF2 = F7 * F11");
        stepInfo.put("function_info", getSubFunctionInfo(2));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": SF2 = F7 * F11");
        if (SF2Flag) {
            flag = F7(step + 1, type) && F11(step + 1, type);
        } else {
            flag = false;
        }
        String resultString = String.valueOf(flag);
        stepInfo = new JSONObject();
        stepInfo.put("id", "SF2");
        stepInfo.put("level", step);
        stepInfo.put("io", "out");
        stepInfo.put("result", resultString);
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "SF2 = F7 * F11 = " + resultString);
        stepInfo.put("function_info", getSubFunctionInfo(2));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": SF2 = F7 * F11 = " + resultString);
        return flag;
    }

    private boolean SF3(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "SF3");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "SF3 = F3 * F14 * (F18 * F19 * F20 OR F21)");
        stepInfo.put("function_info", getSubFunctionInfo(3));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": SF3 = F3 * F14 * (F18 * F19 * F20 OR F21)");
        boolean flag = F3(step + 1, type) && F14(step + 1, type) && (F18(step + 1, type) && F19(step + 1, type) && F20(step + 1, type) || F21(step + 1, type));
        String resultString = String.valueOf(flag);
        stepInfo = new JSONObject();
        stepInfo.put("id", "SF3");
        stepInfo.put("level", step);
        stepInfo.put("io", "out");
        stepInfo.put("result", resultString);
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "SF3 = F3 * F14 * (F18 * F19 * F20 OR F21) = " + resultString);
        stepInfo.put("function_info", getSubFunctionInfo(3));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": SF3 = F3 * F14 * (F18 * F19 * F20 OR F21) = " + resultString);
        return flag;
    }

    private boolean SF4(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "SF4");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "SF4 = F15 * F16 * F22 * F23 * F24");
        stepInfo.put("function_info", getSubFunctionInfo(4));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": SF4 = F15 * F16 * F22 * F23 * F24");
        boolean flag = F15(step + 1, type) && F16(step + 1, type) && F22(step + 1, type) && F23(step + 1, type) && F24(step + 1, type);
        String resultString = String.valueOf(flag);
        stepInfo = new JSONObject();
        stepInfo.put("id", "SF4");
        stepInfo.put("level", step);
        stepInfo.put("io", "out");
        stepInfo.put("result", resultString);
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "SF4 = F15 * F16 * F22 * F23 * F24 = " + resultString);
        stepInfo.put("function_info", getSubFunctionInfo(4));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": SF4 = F15 * F16 * F22 * F23 * F24 = " + resultString);
        return flag;
    }

    private boolean SF5(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "SF5");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "SF5 = F2 * (F18 || F20)");
        stepInfo.put("function_info", getSubFunctionInfo(5));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": SF5 = F2 * (F18 || F20)");
        boolean flag = F2(step + 1, type) && (F18(step + 1, type) || F20(step + 1, type));
        String resultString = String.valueOf(flag);
        stepInfo = new JSONObject();
        stepInfo.put("id", "SF5");
        stepInfo.put("level", step);
        stepInfo.put("io", "out");
        stepInfo.put("result", resultString);
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "SF5 = F2 * (F18 || F20) = " + resultString);
        stepInfo.put("function_info", getSubFunctionInfo(5));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": SF5 = F2 * (F18 || F20) = " + resultString);
        return flag;
    }

    private boolean SF6(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "SF6");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "SF6 = F13 * F17");
        stepInfo.put("function_info", getSubFunctionInfo(6));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": SF6 = F13 * F17");
        boolean flag = F13(step + 1, type) && F17(step + 1, type);
        String resultString = String.valueOf(flag);
        stepInfo = new JSONObject();
        stepInfo.put("id", "SF6");
        stepInfo.put("level", step);
        stepInfo.put("io", "out");
        stepInfo.put("result", resultString);
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "SF6 = F13 * F17 = " + resultString);
        stepInfo.put("function_info", getSubFunctionInfo(6));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": SF6 = F13 * F17 = " + resultString);
        return flag;
    }

    private boolean SF7(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "SF7");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "SF7 = F15 * F16 * F22 * F23 * F24");
        stepInfo.put("function_info", getSubFunctionInfo(7));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": SF7 = F15 * F16 * F22 * F23 * F24");
        boolean flag = F15(step + 1, type) && F16(step + 1, type) && F22(step + 1, type) && F23(step + 1, type) && F24(step + 1, type);
        String resultString = String.valueOf(flag);
        stepInfo = new JSONObject();
        stepInfo.put("id", "SF7");
        stepInfo.put("level", step);
        stepInfo.put("io", "out");
        stepInfo.put("result", resultString);
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "SF7 = F15 * F16 * F22 * F23 * F24 = " + resultString);
        stepInfo.put("function_info", getSubFunctionInfo(7));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": SF7 = F15 * F16 * F22 * F23 * F24 = " + resultString);
        return flag;
    }

    private boolean F1(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "F1");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "F1 = true");
        stepInfo.put("function_info", getBasicFunctionInfo(1));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": F1 = true");
        return true;
    }

    private boolean F2(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "F2");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "F2 = true");
        stepInfo.put("function_info", getBasicFunctionInfo(2));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": F2 = true");
        return true;
    }

    private boolean F3(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "F3");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "F3 = true");
        stepInfo.put("function_info", getBasicFunctionInfo(3));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": F3 = true");
        return true;
    }

    private boolean F4(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "F4");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "F4 = true");
        stepInfo.put("function_info", getBasicFunctionInfo(4));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": F4 = true");
        return true;
    }

    private boolean F5(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "F5");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "F5 = false");
        stepInfo.put("function_info", getBasicFunctionInfo(5));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": F5 = false");
        return false;
    }

    private boolean F6(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "F6");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "F6 = true");
        stepInfo.put("function_info", getBasicFunctionInfo(6));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": F6 = true");
        return true;
    }

    private boolean F7(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "F7");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "F7 = true");
        stepInfo.put("function_info", getBasicFunctionInfo(7));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": F7 = true");
        return true;
    }

    private boolean F8(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "F8");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "F8 = true");
        stepInfo.put("function_info", getBasicFunctionInfo(8));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": F8 = true");
        return true;
    }

    private boolean F9(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "F9");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "F9 = true");
        stepInfo.put("function_info", getBasicFunctionInfo(9));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": F9 = true");
        return true;
    }

    private boolean F10(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "F10");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "F10 = true");
        stepInfo.put("function_info", getBasicFunctionInfo(10));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": F10 = true");
        return true;
    }

    private boolean F11(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "F11");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "F11 = true");
        stepInfo.put("function_info", getBasicFunctionInfo(11));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": F11 = true");
        return true;
    }

    private boolean F12(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "F12");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "F12 = true");
        stepInfo.put("function_info", getBasicFunctionInfo(12));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": F12 = true");
        return true;
    }

    private boolean F13(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "F13");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "F13 = true");
        stepInfo.put("function_info", getBasicFunctionInfo(13));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": F13 = true");
        return true;
    }

    private boolean F14(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "F14");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "F14 = true");
        stepInfo.put("function_info", getBasicFunctionInfo(14));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": F14 = true");
        return true;
    }

    private boolean F15(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "F15");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "F15 = true");
        stepInfo.put("function_info", getBasicFunctionInfo(15));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": F15 = true");
        return true;
    }

    private boolean F16(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "F16");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "F16 = true");
        stepInfo.put("function_info", getBasicFunctionInfo(16));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": F16 = true");
        return true;
    }

    private boolean F17(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "F17");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "F17 = true");
        stepInfo.put("function_info", getBasicFunctionInfo(17));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": F17 = true");
        return true;
    }

    private boolean F18(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "F18");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "F18 = true");
        stepInfo.put("function_info", getBasicFunctionInfo(18));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": F18 = true");
        return true;
    }

    private boolean F19(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "F19");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "F19 = true");
        stepInfo.put("function_info", getBasicFunctionInfo(19));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": F19 = true");
        return true;
    }

    private boolean F20(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "F20");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "F20 = true");
        stepInfo.put("function_info", getBasicFunctionInfo(20));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": F20 = true");
        return true;
    }

    private boolean F21(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "F21");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "F21 = true");
        stepInfo.put("function_info", getBasicFunctionInfo(21));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": F21 = true");
        return true;
    }

    private boolean F22(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "F22");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "F22 = true");
        stepInfo.put("function_info", getBasicFunctionInfo(22));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": F22 = true");
        return true;
    }

    private boolean F23(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "F23");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "F23 = true");
        stepInfo.put("function_info", getBasicFunctionInfo(23));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": F23 = true");
        return true;
    }

    private boolean F24(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "F24");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "F24 = true");
        stepInfo.put("function_info", getBasicFunctionInfo(24));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level" + step + ": F24 = true");
        return true;
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
            analysisProcedure.write(type + " of Monitoring Temperatur is fulfilled!");
        } else {
            analysisProcedure.write(type + " of Monitoring Temperatur is not fulfilled!");
            SF2Flag = false;
            analysisProcedure.write(type + " is setted 'false'");
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
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "SR3 = SF5 = " + resultString);
        stepInfo.put("function_info", getSubRequirementInfo(3));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }
        analysisProcedure.write(type + " Tree Level " + step + ": SR3 = SF5 = " + resultString);
        return flag;
    }

    private boolean BR1(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "BR1");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "BR1 = F7");
        stepInfo.put("function_info", getBasicRequirementInfo(1));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level " + step + ": BR1 = F7");
        return F7(step + 1, type);
    }

    private boolean BR2(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "BR2");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "BR2 = F11");
        stepInfo.put("function_info", getBasicRequirementInfo(2));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level " + step + ": BR2 = F11");
        return F11(step + 1, type);
    }

    private boolean BR3(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "BR3");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "BR3 = F13");
        stepInfo.put("function_info", getBasicRequirementInfo(3));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level " + step + ": BR3 = F13");
        return F13(step + 1, type);
    }

    private boolean BR4(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "BR4");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "BR4 = F17");
        stepInfo.put("function_info", getBasicRequirementInfo(4));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level " + step + ": BR4 = F17");
        return F17(step + 1, type);
    }

    private boolean BR5(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "BR5");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "BR5 = F20");
        stepInfo.put("function_info", getBasicRequirementInfo(5));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level " + step + ": BR5 = F20");
        return F20(step + 1, type);
    }

    private boolean BR6(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "BR6");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "BR6 = F18");
        stepInfo.put("function_info", getBasicRequirementInfo(6));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level " + step + ": BR6 = F18");
        return F18(step + 1, type);
    }

    private boolean BR7(int step, String type) {
        JSONObject stepInfo = new JSONObject();
        stepInfo.put("id", "BR7");
        stepInfo.put("level", step);
        stepInfo.put("io", "in");
        stepInfo.put("result", "unknown");
        stepInfo.put("analysis_type", type);
        stepInfo.put("rule", "BR7 = F2");
        stepInfo.put("function_info", getBasicRequirementInfo(7));
        if (type.equals("Function")) {
            depthSearchAry.put(stepInfo);
        } else {
            requirementDepthSearchAry.put(stepInfo);
        }

        analysisProcedure.write(type + " Tree Level " + step + ": BR7 = F2");
        return F2(step + 1, type);
    }

    private JSONObject getMainFunctionInfo(int i) {
        return mMainFunctions.getJSONObject(i - 1);
    }

    private JSONObject getSubFunctionInfo(int i) {
        return mSubFunctions.getJSONObject(i - 1);
    }

    private JSONObject getBasicFunctionInfo(int i) {
        return mBasicFunctions.getJSONObject(i - 1);
    }

    private JSONObject getMainRequirementInfo(int i) {
        return mMainRequirements.getJSONObject(i - 1);
    }

    private JSONObject getSubRequirementInfo(int i) {
        return mSubRequirements.getJSONObject(i - 1);
    }

    private JSONObject getBasicRequirementInfo(int i) {
        return mBasicRequirements.getJSONObject(i - 1);
    }
}
