package com.project.why.braillelearning.LearningControl;

import android.app.Activity;
import android.content.Context;

import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.BrailleLength;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.Json;

/**
 * Created by hyuck on 2017-09-25.
 */

public class BrailleLearningModuleManager {
    private Activity activity;
    private Context context;
    private Json jsonFileName;
    private Database databaseTableName;
    private BrailleLearningType brailleLearningType;
    private BrailleLength brailleLength;


    BrailleLearningModuleManager(Context context, Activity activity, Json jsonFileName, Database databaseTableName, BrailleLearningType brailleLearningType, BrailleLength brailleLength){
        this.context = context;
        this.activity = activity;
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
                return new BasicLearning(context, activity, jsonFileName, databaseTableName, brailleLearningType, brailleLength);
            case TRANSLATION:
                return new BasicLearning(context, activity, jsonFileName, databaseTableName, brailleLearningType, brailleLength);
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
