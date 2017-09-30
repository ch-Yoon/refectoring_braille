package com.project.why.braillelearning.LearningModel;

import android.content.Context;

import com.project.why.braillelearning.EnumConstant.ServerClient;

import java.util.ArrayList;

/**
 * Created by hyuck on 2017-09-25.
 */

public class BrailleDataManager {
    ArrayList<GettingBraille> brailleArrayList;
    Context context;
    ServerClient serverClientType;
    ArrayList<String> jsonFileNameArray;
    String databaseFileName;

    public BrailleDataManager(Context context, ServerClient serverClientType, ArrayList<String> jsonFileNameArray, String databaseFileName){
        brailleArrayList = new ArrayList<>();
        this.context = context;
        this.serverClientType = serverClientType;
        this.jsonFileNameArray = jsonFileNameArray;
        this.databaseFileName = databaseFileName;
        setBrailleData();
    }

    public void setBrailleData(){
        for(int i=0 ; i<jsonFileNameArray.size() ; i++){
            GettingBraille brailleData;
            if(jsonFileNameArray != null){
                brailleData = new JsonBrailleData(context, jsonFileNameArray.get(i));
            } else {
                if(serverClientType == ServerClient.CLIENT){
                    brailleData = new JsonBrailleData(context, jsonFileNameArray.get(i));
                } else {
                    brailleData = new JsonBrailleData(context, jsonFileNameArray.get(i));
                }
            }
            brailleArrayList.add(brailleData);
        }
    }

    public ArrayList<GettingBraille> getBrailleArrayList(){
        return brailleArrayList;
    }

}
