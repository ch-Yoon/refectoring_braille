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
                return new BasicControl(context, jsonFileName, databaseTableName, brailleLearningType, controlListener);
            case BASIC:
                return new BasicControl(context, jsonFileName, databaseTableName, brailleLearningType, controlListener);
            case MASTER:
                return new BasicControl(context, jsonFileName, databaseTableName, brailleLearningType, controlListener);
            case TRANSLATION:
                return new TranslationControl(context, jsonFileName, databaseTableName, brailleLearningType, controlListener);
            case MYNOTE:
                return new MynoteControl(context, jsonFileName, databaseTableName, brailleLearningType, controlListener);
            case QUIZ:
                return new QuizControl(context, jsonFileName, databaseTableName, brailleLearningType, controlListener);
            case TEACHER:
                return new TeacherControl(context, jsonFileName, databaseTableName, brailleLearningType, controlListener);
            case STUDENT:
                return new StudentControl(context, jsonFileName, databaseTableName, brailleLearningType, controlListener);
            default:
                return null;
        }
    }
}
