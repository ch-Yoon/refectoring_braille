package com.project.why.braillelearning.LearningControl;

import android.content.Context;

import com.project.why.braillelearning.BrailleInformationFactory.GettingInformation;
import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.BrailleLength;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.Json;
import com.project.why.braillelearning.EnumConstant.ServerClient;
import com.project.why.braillelearning.LearningModel.BrailleDataManager;
import com.project.why.braillelearning.LearningModel.GettingBraille;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hyuck on 2017-09-25.
 */

public class BrailleLearningModuleManager {
    private Context context;
    private Json jsonFileName;
    private Database databaseTableName;
    private BrailleLearningType brailleLearningType;
    private BrailleLength brailleLength;


    BrailleLearningModuleManager(Context context, Json jsonFileName, Database databaseTableName, BrailleLearningType brailleLearningType, BrailleLength brailleLength){
        this.context = context;
        this.jsonFileName = jsonFileName;
        this.databaseTableName = databaseTableName;
        this.brailleLearningType = brailleLearningType;
        this.brailleLength = brailleLength;

    }

    public Control getLearningModule(){
        switch(brailleLearningType){
            case TUTORIAL:
                return null;
            case BASIC:
                return new BasicLearningModule(context, jsonFileName, databaseTableName, brailleLearningType, brailleLength);
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
