/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.server.reasoner;

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

    List<String> mTopFunctionsList = new ArrayList<>();
    
    boolean SF2Flag = true;

    public JSONObject analysis(String mFaultLocation) throws SQLException, NamingException {
        JSONArray functions = databaseSystem.getFunctions();
        findTreeTopPoint();
        treeDepthFirstSearchLoop(0);
        return generateResult();
    }

    private void treeDepthFirstSearchLoop(int index) {
        boolean resultFlag = true;
        switch (getTreeTopPoint(index)) {
            case "MF1":
                resultFlag = MF1(1);
                treeDepthFirstSearchLoop(++index);
                break;
            case "MF2":
                resultFlag = MF2(1);
                treeDepthFirstSearchLoop(++index);
                break;
            case "MF3":
                resultFlag = MF3(1);
                treeDepthFirstSearchLoop(++index);
                break;
            case "MF4":
                resultFlag = MF4(1);
                treeDepthFirstSearchLoop(++index);
                break;
            case "SF1":
                resultFlag = SF1(1);
                treeDepthFirstSearchLoop(++index);
                break;
            case "SF2":
                resultFlag = SF2(1);
                treeDepthFirstSearchLoop(++index);
                break;
            case "SF3":
                resultFlag = SF3(1);
                treeDepthFirstSearchLoop(++index);
                break;
            case "SF4":
                resultFlag = SF4(1);
                treeDepthFirstSearchLoop(++index);
                break;
            case "SF5":
                resultFlag = SF5(1);
                treeDepthFirstSearchLoop(++index);
                break;
            case "SF6":
                resultFlag = SF6(1);
                treeDepthFirstSearchLoop(++index);
                break;
            case "SF7":
                resultFlag = SF7(1);
                treeDepthFirstSearchLoop(++index);
                break;
            default:
                analyseRequirement();
                break;
        }
    }

    private String getTreeTopPoint(int index) {
        if (index < mTopFunctionsList.size()) {
            System.out.println("\nDepth First Search Recursive Time: " + (index + 1));
            return mTopFunctionsList.get(index);
        } else {
            return "";
        }
    }

    private void findTreeTopPoint() {
        System.out.println("Function Analysis Step1: Find Top Main Function by searching Functions Relation Table");
        mTopFunctionsList = new ArrayList<>();
        mTopFunctionsList.add("MF1");
        mTopFunctionsList.add("SF1");
        mTopFunctionsList.add("SF2");
    }

    private JSONObject generateResult() {
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
        resultObj.put("basic_functions", BasicFunctions);
        resultObj.put("sub_functions", SubFunctions);
        resultObj.put("main_functions", MainFunctions);
        System.out.println("\nFunction Analysis Result dump:");
        System.out.println("\nBasic Functions Availability:");
        System.out.println(BasicFunctions.toString());
        System.out.println("\nSub Functions Availability:");
        System.out.println(SubFunctions.toString());
        System.out.println("\nMain Functions Availability:");
        System.out.println(MainFunctions.toString());
        return resultObj;
    }

    private void analyseRequirement() {
        System.out.println("\nRequirement Analysis: ");
        if (MR1(1)) {
            System.out.println("\nSystem is runnable!");
        } else {
            System.out.println("\nSystem is not runnable!");
        }
//        System.out.println("\nSF2 <-- SR1");
//        SR1(1);
//        System.out.println("\nSF7 <-- SR2");
//        SR2(2);
//        System.out.println("\nSF5 <-- SR3");
//        SR3(3);
    }

    private boolean MF1(int step) {
        System.out.println("Function Tree Level" + step + ": MF1 = SF1 * SF2");
        boolean flag = SF1(step + 1) && SF2(step + 1);
        String resultString = String.valueOf(flag);
        System.out.println("Function Tree Level" + step + ": MF1 = SF1 * SF2 = " + resultString);
        return flag;
    }

    private boolean MF2(int step) {
        System.out.println("Function Tree Level" + step + ": MF2 = SF3 * SF5 * SF6 * SF7");
        boolean flag = SF3(step + 1) && SF5(step + 1) && SF6(step + 1) && SF7(step + 1);
        String resultString = String.valueOf(flag);
        System.out.println("Function Tree Level" + step + ": MF2 = SF3 * SF5 * SF6 * SF7 = " + resultString);
        return flag;
    }

    private boolean MF3(int step) {
        System.out.println("Function Tree Level" + step + ": MF3 = SF3 * SF5");
        boolean flag = SF3(step + 1) && SF5(step + 1);
        String resultString = String.valueOf(flag);
        System.out.println("Function Tree Level" + step + ": MF3 = SF3 * SF5 = " + resultString);
        return flag;
    }

    private boolean MF4(int step) {
        System.out.println("Function Tree Level" + step + ": MF4 = SF6 * SF7");
        boolean flag = SF6(step + 1) && SF7(step + 1);
        String resultString = String.valueOf(flag);
        System.out.println("Function Tree Level" + step + ": MF4 = SF6 * SF7 = " + resultString);
        return flag;
    }

    private boolean SF1(int step) {
        System.out.println("Function Tree Level" + step + ": SF1 = F1 * F4 * (F5 OR F7)");
        boolean flag = F1(step + 1) && F4(step + 1) && (F5(step + 1) || F7(step + 1));
        String resultString = String.valueOf(flag);
        System.out.println("Function Tree Level" + step + ": SF1 = F1 * F4 * (F5 OR F7) = " + resultString);
        return flag;
    }

    private boolean SF2(int step) {
        boolean flag = true;
        System.out.println("Function Tree Level" + step + ": SF2 = F7 * F11");
        if (SF2Flag) {
            flag = F7(step + 1) && F11(step + 1);
        } else {
            flag = false; 
        }
        String resultString = String.valueOf(flag);
        System.out.println("Function Tree Level" + step + ": SF2 = F7 * F11 = " + resultString);
        return flag;
    }

    private boolean SF3(int step) {
        System.out.println("Function Tree Level" + step + ": SF3 = F3 * F14 * (F18 * F19 * F20 OR F21)");
        boolean flag = F3(step + 1) && F14(step + 1) && (F18(step + 1) && F19(step + 1) && F20(step + 1) || F21(step + 1));
        String resultString = String.valueOf(flag);
        System.out.println("Function Tree Level" + step + ": SF3 = F3 * F14 * (F18 * F19 * F20 OR F21) = " + resultString);
        return flag;
    }

    private boolean SF4(int step) {
        System.out.println("Function Tree Level" + step + ": SF4 = F15 * F16 * F22 * F23 * F24");
        boolean flag = F15(step + 1) && F16(step + 1) && F22(step + 1) && F23(step + 1) && F24(step + 1);
        String resultString = String.valueOf(flag);
        System.out.println("Function Tree Level" + step + ": SF4 = F15 * F16 * F22 * F23 * F24 = " + resultString);
        return flag;
    }

    private boolean SF5(int step) {
        System.out.println("Function Tree Level" + step + ": SF5 = F2 * (F18 || F20)");
        boolean flag = F2(step + 1) && (F18(step + 1) || F20(step + 1));
        String resultString = String.valueOf(flag);
        System.out.println("Function Tree Level" + step + ": SF5 = F2 * (F18 || F20) = " + resultString);
        return flag;
    }

    private boolean SF6(int step) {
        System.out.println("Function Tree Level" + step + ": SF6 = F13 * F17");
        boolean flag = F13(step + 1) && F17(step + 1);
        String resultString = String.valueOf(flag);
        System.out.println("Function Tree Level" + step + ": SF6 = F13 * F17 = " + resultString);
        return flag;
    }

    private boolean SF7(int step) {
        System.out.println("Function Tree Level" + step + ": SF7 = F15 * F16 * F22 * F23 * F24");
        boolean flag = F15(step + 1) && F16(step + 1) && F22(step + 1) && F23(step + 1) && F24(step + 1);
        String resultString = String.valueOf(flag);
        System.out.println("Function Tree Level" + step + ": SF7 = F15 * F16 * F22 * F23 * F24 = " + resultString);
        return flag;
    }

    private boolean F1(int step) {
        System.out.println("Function Tree Level" + step + ": F1 = true");
        return true;
    }

    private boolean F2(int step) {
        System.out.println("Function Tree Level" + step + ": F2 = true");
        return true;
    }

    private boolean F3(int step) {
        System.out.println("Function Tree Level" + step + ": F3 = true");
        return true;
    }

    private boolean F4(int step) {
        System.out.println("Function Tree Level" + step + ": F4 = true");
        return true;
    }

    private boolean F5(int step) {
        System.out.println("Function Tree Level" + step + ": F5 = false");
        return false;
    }

    private boolean F6(int step) {
        System.out.println("Function Tree Level" + step + ": F6 = true");
        return true;
    }

    private boolean F7(int step) {
        System.out.println("Function Tree Level" + step + ": F7 = true");
        return true;
    }

    private boolean F8(int step) {
        System.out.println("Function Tree Level" + step + ": F8 = true");
        return true;
    }

    private boolean F9(int step) {
        System.out.println("Function Tree Level" + step + ": F9 = true");
        return true;
    }

    private boolean F10(int step) {
        System.out.println("Function Tree Level" + step + ": F10 = true");
        return true;
    }

    private boolean F11(int step) {
        System.out.println("Function Tree Level" + step + ": F11 = true");
        return true;
    }

    private boolean F12(int step) {
        System.out.println("Function Tree Level" + step + ": F12 = true");
        return true;
    }

    private boolean F13(int step) {
        System.out.println("Function Tree Level" + step + ": F13 = true");
        return true;
    }

    private boolean F14(int step) {
        System.out.println("Function Tree Level" + step + ": F14 = true");
        return true;
    }

    private boolean F15(int step) {
        System.out.println("Function Tree Level" + step + ": F15 = true");
        return true;
    }

    private boolean F16(int step) {
        System.out.println("Function Tree Level" + step + ": F16 = true");
        return true;
    }

    private boolean F17(int step) {
        System.out.println("Function Tree Level" + step + ": F17 = true");
        return true;
    }

    private boolean F18(int step) {
        System.out.println("Function Tree Level" + step + ": F18 = true");
        return true;
    }

    private boolean F19(int step) {
        System.out.println("Function Tree Level" + step + ": F19 = true");
        return true;
    }

    private boolean F20(int step) {
        System.out.println("Function Tree Level" + step + ": F20 = true");
        return true;
    }

    private boolean F21(int step) {
        System.out.println("Function Tree Level" + step + ": F21 = true");
        return true;
    }

    private boolean F22(int step) {
        System.out.println("Function Tree Level" + step + ": F22 = true");
        return true;
    }

    private boolean F23(int step) {
        System.out.println("Function Tree Level" + step + ": F23 = true");
        return true;
    }

    private boolean F24(int step) {
        System.out.println("Function Tree Level" + step + ": F24 = true");
        return true;
    }

    private boolean MR1(int step) {
        System.out.println("Requirement Tree Level " + step + ": MR1 = (SR2 * SR3) * (SR1 OR (SR2 * SR3))");
        boolean flag = (SR2(step + 1) && SR3(step + 1)) && (SR1(step + 1) || (SR2(step + 1) && SR3(step + 1)));
        String resultString = String.valueOf(flag);
        System.out.println("Requirement Tree Level " + step + ": MR1 = (SR2 * SR3) * (SR1 OR (SR2 * SR3)) = " + resultString);
        return flag;
    }

    private boolean SR1(int step) {
        System.out.println("Requirement Tree Level " + step + ": SR1 = SF2");
        boolean flag = SF2(step + 1);
        String resultString = String.valueOf(flag);
        System.out.println("Requirement Tree Level " + step + ": SR1 = SF2 = " + resultString);
        if (flag) {
            System.out.println("Safety of Monitoring Temperatur is fulfilled!");
        } else {
            System.out.println("Safety of Monitoring Temperatur is not fulfilled!");
            SF2Flag = false;
            System.out.println("SF2 is setted 'false'");
        }
        return flag;
    }

    private boolean SR2(int step) {
        System.out.println("Requirement Tree Level " + step + ": SR2 = SF7");
        boolean flag = SF7(step + 1);
        String resultString = String.valueOf(flag);
        System.out.println("Requirement Tree Level " + step + ": SR2 = SF7 = " + resultString);
        return flag;
    }

    private boolean SR3(int step) {
        System.out.println("Requirement Tree Level " + step + ": SR3 = SF5");
        boolean flag = SF5(step + 1);
        String resultString = String.valueOf(flag);
        System.out.println("Requirement Tree Level " + step + ": SR3 = SF5 = " + resultString);
        return flag;
    }

    private boolean BR1(int step) {
        System.out.println("Requirement Tree Level " + step + ": BR1 = F7");
        return F7(step + 1);
    }

    private boolean BR2(int step) {
        System.out.println("Requirement Tree Level " + step + ": BR2 = F11");
        return F11(step + 1);
    }

    private boolean BR3(int step) {
        System.out.println("Requirement Tree Level " + step + ": BR3 = F13");
        return F13(step + 1);
    }

    private boolean BR4(int step) {
        System.out.println("Requirement Tree Level " + step + ": BR4 = F17");
        return F17(step + 1);
    }

    private boolean BR5(int step) {
        System.out.println("Requirement Tree Level " + step + ": BR5 = F20");
        return F20(step + 1);
    }

    private boolean BR6(int step) {
        System.out.println("Requirement Tree Level " + step + ": BR6 = F18");
        return F18(step + 1);
    }

    private boolean BR7(int step) {
        System.out.println("Requirement Tree Level " + step + ": BR7 = F2");
        return F2(step + 1);
    }
}
