package com.project.why.braillelearning.LearningModel;

import android.content.Context;
import android.content.res.AssetManager;
import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Json;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by hyuck on 2017-09-12.
 */

/**
 * json file로부터 점자 data를 읽어오는 class
 */
public class JsonBrailleData implements GettingBraille {
    // 점자 데이터 Manager
    private Json jsonFileName;
    protected ArrayList<BrailleData> brailleDataArray = new ArrayList<>(); // initial json file read
    private Context context;
    private BrailleLearningType brailleLearningType;

    public JsonBrailleData(Context context, Json jsonFileName, BrailleLearningType brailleLearningType) {
        this.context = context;
        this.jsonFileName = jsonFileName;
        this.brailleLearningType = brailleLearningType;
        readJsonBraillData();
    }


    /**
     * json file로부터 data를 읽어와 BrailleData에 저장 후, 각각의 BrailleData들을 Arraylist에 저장함
     */
    private void readJsonBraillData(){ // json 읽기
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
        }

    }


    /**
     * 점자 데이터를 저장하는 함수
     * @param letterName : 점자를 의미하는 글자
     * @param brailleMatrix : 점자 행렬을 의미하는 string
     * @param assistanceLetterName : 퀴즈를 정답을 위한 글자
     * @param rawId : 음성 파일 id
     */
    private void addBrailleDataArray(String letterName, String brailleMatrix, String assistanceLetterName, String rawId){
        BrailleData data = new BrailleData(letterName, brailleMatrix, assistanceLetterName, rawId, brailleLearningType);
        brailleDataArray.add(data);
    }

    /**
     * 저장되어진 braille data arraylist를 return하는 함수
     * @return braiiledataArray 리턴
     */
    @Override
    public ArrayList<BrailleData> getBrailleDataArray(){
        return brailleDataArray;
    }
}
