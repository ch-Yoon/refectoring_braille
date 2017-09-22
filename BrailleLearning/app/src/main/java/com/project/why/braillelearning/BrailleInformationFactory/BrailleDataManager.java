package com.project.why.braillelearning.BrailleInformationFactory;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by hyuck on 2017-09-12.
 */

public class BrailleDataManager {
    // 점자 데이터 Manager
    private String BrailleDataName;
    private ArrayList<BrailleData> BrailleDataArray = new ArrayList<>(); // initial json file read
    private Context mContext;

    public BrailleDataManager(Context context, String JsonFileName) {
        mContext = context;
        readJsonBraillData(JsonFileName);
    }

    public void readJsonBraillData(String JsonFileName){ // json 읽기
        try {
            if(JsonFileName != null) {
                AssetManager assetManager = mContext.getAssets();
                AssetManager.AssetInputStream ais = (AssetManager.AssetInputStream) assetManager.open("json/" + JsonFileName + ".json");
                BufferedReader br = new BufferedReader(new InputStreamReader(ais));

                String ReadText = "";
                String jsonString = "";
                while ((ReadText = br.readLine()) != null) {
                    jsonString += ReadText;
                }

                JSONObject jsonObject = new JSONObject(jsonString);

                JSONArray jArr = new JSONArray(jsonObject.getString(JsonFileName));
                for (int i = 0; i < jArr.length(); i++) {
                    String Lettername = jArr.getJSONObject(i).getString("LetterName").toString();
                    String BrailleMatrix = jArr.getJSONObject(i).getString("BrailleMatrix").toString();
                    String AssistanceLetterName = jArr.getJSONObject(i).getString("AssistanceName").toString();
                    addBrailleDataArray(Lettername, BrailleMatrix, AssistanceLetterName);
                }
            }
        } catch (Exception e) {
            Log.d("BrailleDataManager",e.getMessage());
        }

    }

    public void addBrailleDataArray(String LetterName, String BrailleMatrix, String AssistanceLetterName){
        BrailleData Data = new BrailleData(LetterName, BrailleMatrix, AssistanceLetterName);
        BrailleDataArray.add(Data);
    }

    public ArrayList<BrailleData> getBrailleDataArray(){
        return BrailleDataArray;
    }
}
