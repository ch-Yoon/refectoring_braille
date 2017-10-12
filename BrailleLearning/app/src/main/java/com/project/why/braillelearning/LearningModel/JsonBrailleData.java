package com.project.why.braillelearning.LearningModel;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.project.why.braillelearning.EnumConstant.Json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by hyuck on 2017-09-12.
 */

public class JsonBrailleData implements GettingBraille {
    // 점자 데이터 Manager
    private Json jsonFileName;
    private ArrayList<BrailleData> brailleDataArray = new ArrayList<>(); // initial json file read
    private Context context;

    public JsonBrailleData(Context context, Json jsonFileName) {
        this.context = context;
        this.jsonFileName = jsonFileName;
        readJsonBraillData();
    }

    public void readJsonBraillData(){ // json 읽기
        try {
            AssetManager assetManager = context.getAssets();
            AssetManager.AssetInputStream ais = (AssetManager.AssetInputStream) assetManager.open("json/" + jsonFileName.getName() + ".json");
            BufferedReader br = new BufferedReader(new InputStreamReader(ais));

            String ReadText = "";
            String jsonString = "";
            while ((ReadText = br.readLine()) != null) {
                jsonString += ReadText;
            }

            JSONObject jsonObject = new JSONObject(jsonString);

            JSONArray jArr = new JSONArray(jsonObject.getString(jsonFileName.getName()));
            for (int i = 0; i < jArr.length(); i++) {
                String lettername = jArr.getJSONObject(i).getString("LetterName").toString();
                String brailleMatrix = jArr.getJSONObject(i).getString("BrailleMatrix").toString();
                String assistanceLetterName = jArr.getJSONObject(i).getString("AssistanceName").toString();
                String rawId = jArr.getJSONObject(i).getString("RawId").toString();
                addBrailleDataArray(lettername, brailleMatrix, assistanceLetterName, rawId);
            }
        } catch (Exception e) {
            Log.d("JsonBrailleData",e.getMessage());
        }

    }

    public void addBrailleDataArray(String letterName, String brailleMatrix, String assistanceLetterName, String rawId){
        BrailleData data = new BrailleData(letterName, brailleMatrix, assistanceLetterName, rawId);
        brailleDataArray.add(data);
    }

    public String getJsonFileName(){
        return jsonFileName.getName();
    }

    @Override
    public ArrayList<BrailleData> getBrailleDataArray(){
        return brailleDataArray;
    }
}
