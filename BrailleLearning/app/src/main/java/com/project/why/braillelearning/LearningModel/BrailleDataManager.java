package com.project.why.braillelearning.LearningModel;

import android.content.Context;

import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.Json;
import com.project.why.braillelearning.EnumConstant.ServerClient;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hyuck on 2017-09-25.
 */

public class BrailleDataManager {
    private Context context;
    private Json jsonFileName;
    private Database databaseFileName;
    private BrailleLearningType brailleLearningType;

    public BrailleDataManager(Context context, Json jsonFileName, Database databaseFileName, BrailleLearningType brailleLearningType){
        this.context = context;
        this.jsonFileName = jsonFileName;
        this.databaseFileName = databaseFileName;
        this.brailleLearningType = brailleLearningType;
    }

    public GettingBraille getBrailleArrayList(){
        switch(brailleLearningType){
            case TUTORIAL:
                return null;
            case BASIC:
                return new JsonBrailleData(context, jsonFileName);
            case TRANSLATION:
                return null;
            case READING_QUIZ:
                return null;
            case WRITING_QUIZ:
                return null;
            case MYNOTE:
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
