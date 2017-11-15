package com.project.why.braillelearning.LearningControl;

import android.content.Context;

import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.Json;

/**
 * Created by hyuck on 2017-09-25.
 */

public class BrailleLearningModuleManager {
    private Context context;
    private ControlListener controlListener;
    private Json jsonFileName;
    private Database databaseTableName;
    private BrailleLearningType brailleLearningType;

    BrailleLearningModuleManager(Context context, ControlListener controlListener, Json jsonFileName, Database databaseTableName, BrailleLearningType brailleLearningType){
        this.context = context;
        this.controlListener = controlListener;
        this.jsonFileName = jsonFileName;
        this.databaseTableName = databaseTableName;
        this.brailleLearningType = brailleLearningType;
    }

    public Control getLearningModule(){
        switch(brailleLearningType){
            case TUTORIAL:
                return null;
            case BASIC:
                return new BrailleControl(context, controlListener, jsonFileName, databaseTableName, brailleLearningType);
            case TRANSLATION:
                return new BrailleControl(context, controlListener,  jsonFileName, databaseTableName, brailleLearningType);
            case MYNOTE:
                return new BrailleControl(context, controlListener,  jsonFileName, databaseTableName, brailleLearningType);
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
