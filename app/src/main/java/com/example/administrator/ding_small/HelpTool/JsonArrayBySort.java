package com.example.administrator.ding_small.HelpTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2017/11/29.
 */

public class JsonArrayBySort {
    public static JSONArray sort(JSONArray ja, final String field, boolean isAsc){
        List<JSONObject> sortList=new ArrayList<>();
        for (int i=0;i<ja.length();i++){
            try {
                sortList.add((JSONObject) ja.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(sortList, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject o1, JSONObject o2) {
                Object f1 = null;
                Object f2 = null;
                try {
                    f1 = o1.getString(field);
                    f2 = o2.getString(field);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (f1 instanceof Number && f2 instanceof Number) {
                    return ((Number) f1).intValue() - ((Number) f2).intValue();
                } else {
                    return f1.toString().compareTo(f2.toString());
                }
            }
        });
        if(!isAsc){
            Collections.reverse(sortList);
        }
        JSONArray result=new JSONArray();
        for(int i=0;i<sortList.size();i++){
            result.put(sortList.get(i));
        }
        return result;
    }
}
