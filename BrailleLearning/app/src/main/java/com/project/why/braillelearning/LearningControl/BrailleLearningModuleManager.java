package com.project.why.braillelearning.LearningControl;

import android.content.Context;

import com.project.why.braillelearning.BrailleInformationFactory.GettingInformation;
import com.project.why.braillelearning.BrailleInformationFactory.Tutorial;
import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.ServerClient;
import com.project.why.braillelearning.LearningModel.BrailleDataManager;
import com.project.why.braillelearning.LearningModel.GettingBraille;

import java.util.ArrayList;

/**
 * Created by hyuck on 2017-09-25.
 */

public class BrailleLearningModuleManager {
    BrailleLearningType brailleLearningType;
    ArrayList<GettingBraille> brailleArrayList;

    BrailleLearningModuleManager(Context context, ServerClient serverClient, ArrayList<String> jsonFileNameArray, String databaseTableName, BrailleLearningType brailleLearningType){
        this.brailleArrayList = getBrailleArrayList(context, serverClient, jsonFileNameArray, databaseTableName);
        this.brailleLearningType = brailleLearningType;
    }

    public ArrayList<GettingBraille> getBrailleArrayList(Context context, ServerClient serverClient, ArrayList<String> jsonFileNameArray, String databaseTableName){
        BrailleDataManager brailleDataManager = new BrailleDataManager(context, serverClient, jsonFileNameArray, databaseTableName);
        return brailleDataManager.getBrailleArrayList();
    }

    public FingerFunction getLearningModule(){
        switch(brailleLearningType){
            case TUTORIAL:
                return null;
            case BASIC:
                return new BasicLearningModule(brailleArrayList);
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
