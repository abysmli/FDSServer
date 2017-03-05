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

    List<String> mTopFunctionsList = new ArrayList<String>();

    public JSONObject analysis(String mFaultLocation) throws SQLException, NamingException {

        JSONArray functions = databaseSystem.getFunctions();
        JSONObject resultObj = new JSONObject();
        JSONArray BasicFunctions = new JSONArray();
        JSONArray SubFunctions = new JSONArray();
        JSONArray MainFunctions = new JSONArray();
        this.mTopFunctionsList = findTreeTopPoint();
        treeTopPointLoop(0);
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

    private void treeTopPointLoop(int index) {
        boolean resultFlag = true;
        switch (getTreeTopPoint(index)) {
            case "MF1":
                resultFlag = MF1(1);
                treeTopPointLoop(++index);
                break;
            case "MF2":
                resultFlag = MF2(1);
                treeTopPointLoop(++index);
                break;
            case "MF3":
                resultFlag = MF3(1);
                treeTopPointLoop(++index);
                break;
            case "SF1":
                resultFlag = SF1(1);
                treeTopPointLoop(++index);
                break;
            case "SF2":
                resultFlag = SF2(1);
                treeTopPointLoop(++index);
                break;
            case "SF3":
                resultFlag = SF3(1);
                treeTopPointLoop(++index);
                break;
            case "BF4":
                resultFlag = BF4(1);
                treeTopPointLoop(++index);
                break;
            case "BF5":
                resultFlag = BF5(1);
                treeTopPointLoop(++index);
                break;
            default:
                break;
        }
    }

    private String getTreeTopPoint(int index) {
        if (index < mTopFunctionsList.size()) {
            System.out.println("\nDepth First Search Loop Time: " + (index + 1));
            return mTopFunctionsList.get(index);
        } else {
            return "";
        }
    }

    private List<String> findTreeTopPoint() {
        System.out.println("Function Analysis Step1: Find Top Main Function by searching Functions Relation Table");
        List<String> mTopFunctionsList = new ArrayList<>();
        mTopFunctionsList.add("MF1");
        mTopFunctionsList.add("SF1");
        return mTopFunctionsList;
    }

    private boolean MF1(int step) {
        System.out.println("Tree Level " + step + ": MF1 = SF1 * SF2 * SF3");
        return SF1(++step) && SF2(++step) && SF3(++step);
    }

    private boolean SF1(int step) {
        System.out.println("Tree Level " + step + ": SF1 = F4 * (F5 or F21)");
        return F4(++step) && (F5(++step) || F21(++step));
    }

    private boolean SF2(int step) {
        System.out.println("Tree Level " + step + ": SF2 = true");
        return true;
    }

    private boolean SF3(int step) {
        System.out.println("Tree Level " + step + ": SF3 = true");
        return true;
    }

    private boolean F4(int step) {
        System.out.println("Tree Level " + step + ": F4 = true");
        return true;
    }

    private boolean F5(int step) {
        System.out.println("Tree Level " + step + ": F5 = false");
        return false;
    }

    private boolean F21(int step) {
        System.out.println("Tree Level " + step + ": F21 = true");
        return true;
    }

    private boolean MF2(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean MF3(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean BF4(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean BF5(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
