package com.project.why.braillelearning.LearningControl;

import android.content.Context;

import com.project.why.braillelearning.BrailleInformationFactory.GettingInformation;
import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.BrailleLength;
import com.project.why.braillelearning.EnumConstant.ServerClient;
import com.project.why.braillelearning.LearningModel.BrailleDataManager;
import com.project.why.braillelearning.LearningModel.GettingBraille;

import java.util.ArrayList;

/**
 * Created by hyuck on 2017-09-25.
 */

public class BrailleLearningModuleManager {
    Context mContext;
    ServerClient serverClient;
    ArrayList<String> jsonFileNameArray;
    String databaseTableName;
    BrailleLearningType brailleLearningType;
    BrailleLength brailleLength;
    ArrayList<GettingBraille> brailleArrayList;

    BrailleLearningModuleManager(Context context, GettingInformation object){
        mContext = context;
        serverClient = object.getServerClientType();
        jsonFileNameArray = object.getJsonFileNameArray();
        databaseTableName = object.getDatabaseTableName();
        brailleLearningType = object.getBrailleLearningType();
        brailleLength = object.gettBrailleLength();

        initBrailleArrayList();
    }

    public void initBrailleArrayList(){
        BrailleDataManager brailleDataManager = new BrailleDataManager(mContext, serverClient, jsonFileNameArray, databaseTableName);
        brailleArrayList = brailleDataManager.getBrailleArrayList();
    }

    public Control getLearningModule(){
        switch(brailleLearningType){
            case TUTORIAL:
                return null;
            case BASIC:
                return new BasicLearningModule(mContext, brailleArrayList, brailleLength);
            case TRANSLATION:
                return null;
            case READING_QUIZ:
                return null;
            case WRITING_QUIZ:
                return null;
            case TEACHER:
                return null;
            case STUDENT:
                return null;
            default:
                return null;
        }

    }
}
